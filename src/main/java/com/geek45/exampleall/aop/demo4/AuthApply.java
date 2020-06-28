package com.geek45.exampleall.aop.demo4;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("aop")
public class AuthApply {

    @Authon(auth = "admin")
    @RequestMapping("test")
    public String testAop(){
        return "成功";
    }
}
