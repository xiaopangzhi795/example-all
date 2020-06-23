package com.geek45.exampleall.aop.demo2;

import org.springframework.stereotype.Service;

@Service("aceService")
public class TestHandler extends AbstractHandler {

    @Override
    public String handler(String type) {
        return type + ":你好";
    }

}
