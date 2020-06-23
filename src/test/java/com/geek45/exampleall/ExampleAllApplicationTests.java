package com.geek45.exampleall;

import com.geek45.exampleall.aop.demo1.AbstractHandler;
import com.geek45.exampleall.aop.demo1.HandlerContext;
import com.geek45.exampleall.aop.demo2.HandlerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ExampleAllApplicationTests {


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
