package com.geek45.exampleall.sync.api;

import com.geek45.exampleall.lock.redis.CacheTest;
import com.geek45.exampleall.util.ThreadPoolUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@CacheConfig(cacheNames = "async")
public class AsyncController {

    private static String DEFAULT_MESSAGE = "正在认证，请耐心等待，稍后刷新看结果！";

    //线程池
    public static ThreadPoolExecutor FIXED_THREAD_POOL = ThreadPoolUtil.createDefaultThreadPool(20);

    private Map<String, Boolean> cache = new ConcurrentHashMap<>();

    @Resource
    private CacheTest cacheTest;

    /**
     * 异步接口demo
     * 适用场景
     *      客户要求必须再规定时间内返回数据，不管是否成功。
     *      不能立即返回，也不能超时太久，等客户那边超时。
     *      在接口处使用spring 的异步接口机制，可以设置一个超时时间，在时间范围内，只要有结果了，就可以得到返回值。
     *      如果超时了，返回一个定义好的默认值。
     *      客户传过来的时候设置一个标识，这个标识用来规定这个用户的相同请求。
     *      分三个状态：
     *      1、用户第一次请求，判断缓存中有没有用户的请求信息，来确定是否为第一次请求。如果是第一次请求，将标识放进缓存。
     *      2、用户非第一次请求，但是数据还没准备好，直接返回给用户默认的消息，比如告知用户正在处理中。通过判断缓存中标识的状态进行判断是否正在处理，防止重复处理。
     *      3、用户非第一次请求，数据已经准备好，直接将数据返回给客户，数据处理好之后直接放在缓存中，用户下一次请求过来的时候，直接从缓存里面获取数据。
     *
     * @param flag
     * @return
     */
    @RequestMapping("/async/{flag}")
    public DeferredResult<String> asyncApi(@PathVariable("flag") String flag){
        String uid = UUID.randomUUID().toString();
        ResponseEntity<String>
                NOT_MODIFIED_RESPONSE = ResponseEntity.status(HttpStatus.NOT_MODIFIED.value()).body(DEFAULT_MESSAGE);
        DeferredResult<String> deferredResult = new DeferredResult<>(1000L,NOT_MODIFIED_RESPONSE.getBody());
        //当deferredResult完成时（不论是超时还是异常还是正常完成），移除watchRequests中相应的watch key
        deferredResult.onCompletion(() -> System.err.println(String.format("【%s】执行结束，返回结果是：%s", uid, deferredResult.getResult())));
        deferredResult.onTimeout(() -> System.err.println(String.format("【%s】执行超时，返回结果是：%s", uid, deferredResult.getResult())));
        FIXED_THREAD_POOL.execute(()->{
            if (!cache.containsKey(flag)) {
                cache.put(flag, false);
            }
            String result = cacheTest.test(flag);
            deferredResult.setResult(result);
        });
        return deferredResult;
    }

}
