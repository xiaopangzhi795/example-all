package com.geek45.exampleall.aspect.demo2;

import org.springframework.stereotype.Component;

/**
 * @Author: Lingye
 * @Date: 2018/11/11
 * @Describe:
 * @Modified By:
 */
@Component
public class LoggerApply {

    @Lingyejun(module = "http://www.cnblogs.com/lingyejun/")
    public void lingLogger(String event) throws Exception {
//        System.out.println("lingLogger(String event) : lingyejun will auth by blog address:" + event);
        System.err.println("主方法");
//        System.err.println("不打印异常");
        System.err.println("打印异常");
        throw new Exception();
    }

    public LoggerApply(){}
}
