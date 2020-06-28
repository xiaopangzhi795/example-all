package com.geek45.exampleall.strategy.demo2;

public abstract class AbstractHandler implements HandlerService{

    String message = ":不支持的接口";

    @Override
    public String handler(String type) {
        return type + message;
    }
}
