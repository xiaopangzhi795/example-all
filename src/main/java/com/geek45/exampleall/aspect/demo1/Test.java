package com.geek45.exampleall.aspect.demo1;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Test {
    String name() default "小明";
    int age() default 19;
}
