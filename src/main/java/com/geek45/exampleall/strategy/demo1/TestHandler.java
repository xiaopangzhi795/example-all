package com.geek45.exampleall.strategy.demo1;


import org.springframework.stereotype.Component;

@HandlerType("ace")
@Component
public class TestHandler extends AbstractHandler {
    @Override
    public String handler(String type) {
        return type + ":Ace";
    }
}
