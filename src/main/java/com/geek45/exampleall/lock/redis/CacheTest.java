package com.geek45.exampleall.lock.redis;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@CacheConfig(cacheNames = "TEST")
public class CacheTest {

    private Map<String, Boolean> cache = new ConcurrentHashMap<>();

    /**
     * 测试接口异步获取单条数据
     * @param flag
     * @return
     */
    @Cacheable(cacheNames = "'test'",key = "#flag")
    public String test(String flag){
        if (!cache.containsKey(String.format("pose:%s", flag))) {
            cache.put(String.format("pose:%s", flag), true);
        }else{
            if (cache.get(String.format("pose:%s", flag))) {
                return "正在处理中";
            }
        }
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cache.remove(String.format("pose:%s", flag));
        return "hello";
    }

    /**
     * 测试查询，将查询结果缓存
     * @return
     */
    @Cacheable(key = "'ALL'")
    public List<String> testCache(){
        List<String> test = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            test.add(UUID.randomUUID().toString());
            try {
                Thread.sleep(100l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return test;
    }

    /**
     * 测试传参，将参数缓存
     * @param testBean
     */
    public void testCache(TestBean testBean) {

    }

}

class TestBean{
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}