package com.geek45.exampleall.aop.demo1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerContext {
    private static final Logger log = LoggerFactory.getLogger(HandlerContext.class);

    private Map<String, Class> handlerMap;
    private List<String> handlerList;

    public HandlerContext(List<String> handlerList) {
        log.info("初始化HandlerContext");
        this.handlerList = handlerList;
    }

    public AbstractHandler getInstance(String type) {
        if (handlerMap == null) {
            log.info("handlerMap为空，初始化");
            handlerMap = new HashMap<>();
            for (String beanName : handlerList) {
                Class clazz = SpringContextUtil.getBean(beanName).getClass();
                handlerMap.put(getAnnotation(clazz), clazz);
            }
        }else{
            log.info("handlerMap不为空，不需要初始化");
        }
        Class clazz = handlerMap.get(type);
        if (clazz == null) {
            throw new RuntimeException("没有找到该实例");
        }
        return (AbstractHandler) SpringContextUtil.getBean(clazz);
    }

    public static String getAnnotation(Class clazz) {
        HandlerType handlerType = (HandlerType) clazz.getAnnotation(HandlerType.class);
        if (handlerType != null) {
            return handlerType.value();
        } else {
            return "";
        }
    }
}
