package com.geek45.exampleall.aop.demo3;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Name {
    String name() default "老钱";
    int age() default 24;
}
