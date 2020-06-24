package com.geek45.exampleall.aspect.demo1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;

@Component
public class TestAspect implements AspectTest, Serializable {
    private static final Logger log = LoggerFactory.getLogger(TestAspect.class);

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        log.info("开始前");
        return false;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args) {
        log.info("开始后");
        return false;
    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        log.info("开始后异常");
        return false;
    }
}
