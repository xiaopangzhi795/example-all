package com.geek45.exampleall.lock.redis;

import com.geek45.exampleall.util.ThreadPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
@Component
public class Distributed {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 思路：
     * 开始之前，同时休眠，5秒后，同时开抢
     *      锁必须一致，3k线程争抢一个锁
     *      拿到锁后，随机休眠1-5秒，将商品数量减一，模仿下单操作
     *      锁的超时时间3秒
     *      真正操作前判断是否超时
     *
     *      结束后将锁释放。释放的时候判断是否是自己的锁。
     */
    public void testMultiThread(){
        long startTime = System.currentTimeMillis();    //标识一个时间，所有线程都以这个时间为主，5秒后，同时开抢
        Map<String, Integer> shopping = new ConcurrentHashMap<>();
        shopping.put("shop", 10);
        String key = "iPhone："+UUID.randomUUID().toString().replaceAll("-","");

        //数据准备好，开始准备3k线程
        ThreadPoolExecutor executor = ThreadPoolUtil.createDefaultThreadPool(5000);
        for (int i = 0; i < 5000; i++) {
            executor.execute(()->{
                String uuid = Thread.currentThread().getName() + ":" + UUID.randomUUID().toString().replaceAll("-", "");
                rtyAction:
                while (shopping.get("shop") > 0) {
                    if (!isAction(startTime)) {
                        continue;
                    } else {
                        int shopNum = shopping.get("shop");
                        if (shopNum < 0) {
                            System.out.print(uuid + "：非常抱歉，商品数量不足，下次再来吧！\t");
                            break rtyAction;
                        }
                        //开始下单逻辑
                        Lock lock = testLock(key, uuid, 500, 3);
                        try {
                            if (lock.isLocked()) {
                                //拿到锁，开始下单
                                shopNum = shopping.get("shop");
                                if (shopNum > 0) {
                                    shopping.put("shop", shopNum - 1);
                                    System.err.println("恭喜"+uuid+"->用3999的价格抢到了一台iPhone 11 pro max");
                                }else{
                                    System.out.print(uuid + "：非常抱歉，商品数量不足，下次再来吧！\t");
                                }
                            }
                        } finally {
                            try {
                                if (!unlock(lock)) {
                                    System.err.println("解锁失败");
                                }
                            } catch (Exception e) {
                                System.err.println(uuid + "解锁失败，已经超过了购买时间。");
                            }
                        }
                    }
                }
                System.out.print(uuid + "：没抢到！\t");
            });
        }

        while (true) {
            try {
                Thread.sleep(1000l);
                System.out.println("还有" + shopping.get("shop"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isAction(long startTime) {
        return (System.currentTimeMillis() - startTime) > 5000;
    }

    public static void main(String[] args) {
        ThreadPoolExecutor executor = ThreadPoolUtil.createDefaultThreadPool(1);
        executor.execute(() -> System.err.println("ok"));
    }

    /**
     * 测试加锁，解锁
     */
    public void test(){
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        String uuid = Thread.currentThread().getName();
        Lock lock = testLock(key, uuid, 5000, 60);
        if (lock.isLocked()) {
            int i = 0;
            try {
                tryUnlock:
                while (!unlock(lock)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i++>10)
                        break tryUnlock;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 尝试获取锁
     * @param key       锁名
     * @param value     锁值
     * @param getTimeOut    获取锁超时时间  毫秒
     * @param expires       锁过期时间     秒
     * @return
     */
    private Lock testLock(String key, String value, long getTimeOut, long expires) {
        long start = System.currentTimeMillis();
        boolean locked = false;
        tryGetLock:
        do {
            locked = redisTemplate.opsForValue().setIfAbsent(
                    key,
                    value,
                    expires, TimeUnit.SECONDS
            );
            if (locked) {
                break tryGetLock;
            }
        } while ((System.currentTimeMillis() - start) < getTimeOut);
        return Lock.createLock(locked, key, value);
    }

    private boolean unlock(Lock lock) throws Exception {
        if (lock.isLocked()) {
            String value = (String) redisTemplate.opsForValue().get(lock.getLockKey());
            if (lock.isSelfLock(value)) {
                //如果是自己的锁 就解锁
                return redisTemplate.delete(lock.getLockKey());
            }
            throw new Exception("非自己的锁");
        } else {
            return true;
        }
    }



}

class Lock{
    private boolean locked;
    private String lockKey;
    private String lockValue;
    private Lock(){}

    private Lock(boolean locked, String lockKey,String lockValue) {
        this.lockKey = lockKey;
        this.locked = locked;
        this.lockValue = lockValue;
    }

    static Lock createLock(boolean locked, String lockKey,String lockValue) {
        return new Lock(locked, lockKey, lockValue);
    }

    public boolean isSelfLock(String lockV) {
        return StringUtils.isNotBlank(lockV) && lockValue.equals(this.lockValue);
    }
    public boolean isLocked(){
        return this.locked;
    }
    public String getLockKey(){
        return this.lockKey;
    }
}
