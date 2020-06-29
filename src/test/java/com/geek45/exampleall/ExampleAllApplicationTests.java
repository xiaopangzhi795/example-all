package com.geek45.exampleall;

import com.geek45.exampleall.aop.demo1.TestMain;
import com.geek45.exampleall.aop.demo2.LoggerApply;
import com.geek45.exampleall.aop.demo3.RubikApply;
import com.geek45.exampleall.lock.redis.CacheTest;
import com.geek45.exampleall.lock.redis.Distributed;
import com.geek45.exampleall.strategy.demo1.AbstractHandler;
import com.geek45.exampleall.strategy.demo1.HandlerContext;
import com.geek45.exampleall.strategy.demo2.HandlerService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleAllApplication.class)
class ExampleAllApplicationTests {

    @Resource
    private Distributed distributed;

    @Resource
    private CacheTest cacheTest;

    @Test
    public void cacheTest(){
        cacheTest.testCache();
    }

    @Test
    public void testSeckill(){
        distributed.testMultiThread();
    }

    @Test
    public void testLock(){
        distributed.test();
    }

    @Resource
    private RubikApply rubikApply;

    @Test
    public void rubikTest() {
        try {
            rubikApply.say("老钱");
        } catch (Exception e) {
            System.out.println("ssss");
        }
    }


    @Autowired
    private LoggerApply loggerApply;

    @Test
    public void testAnnotationLogger() {
        try {
            loggerApply.lingLogger("Blog Home");
        } catch (Exception e) {
            System.out.println("a exception be there");
        }
    }


    @Resource
    private TestMain aspect;

    @Test
    void testAspect(){
        aspect.test1();
        aspect.test2();
        aspect.test3();
    }


    @Resource
    private HandlerContext handlerContext;

    @Test
    void contextLoads() {
        AbstractHandler handler = handlerContext.getInstance("ace");
        System.err.println(handler.handler("你好"));
    }

    @Resource
    @Qualifier("default")
    HandlerService handlerService;

    @Test
    void testHandler(){
        System.err.println(handlerService.handler("ace"));
        System.err.println(handlerService.handler("qian"));
        System.err.println(handlerService.handler("www"));
    }

}
