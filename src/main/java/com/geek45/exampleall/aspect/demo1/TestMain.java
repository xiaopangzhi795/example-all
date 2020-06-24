package com.geek45.exampleall.aspect.demo1;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

/**
 * 使用切面的形式，执行一个方法，查看效果
 */
@Aspect
@Configuration
public class TestMain {

    @Pointcut("execution(* com.geek45.exampleall.aspect.demo1..*.*(..))")
    public void test1(){
        System.err.println("i am test1()");
    }

    @Before("test1()")
    public void test2(){
        System.err.println("i am test2()");
    }

    @After("test1()")
    public void test3(){
        System.err.println("i am test3()");
    }

    public static void main(String[] args) {
        TestMain testMain = new TestMain();
        testMain.test1();
        testMain.test2();
        testMain.test3();
    }

}
