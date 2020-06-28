package com.geek45.exampleall.aop.demo3;

import org.springframework.stereotype.Component;

@Component
public class RubikApply {
    /**
     * 切面实现的主方法
     * <p>
     * 最终会去执行这个方法，执行这个方法的同时，执行一些其他方法
     */
    @Name(name = "晗晗", age = 18)
    public void say(String name) throws Exception {
        System.err.println("你看我美吗？" + name);
        throw new Exception("害！");
    }
}
