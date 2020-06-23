package com.geek45.exampleall.aop.demo2;

import com.geek45.exampleall.aop.demo1.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("default")
public class HandlerServiceImpl implements HandlerService {

    private Map<String, AbstractHandler> beanMap = null;
    @Resource
    private HandlerConfig config;

    private HandlerService getBeanByType(String type) {
        if (StringUtils.isNotBlank(type)) {
            if (beanMap == null) {
                beanMap = SpringContextUtil.getBeanOfType(AbstractHandler.class);
            }
            if (config == null) {
                return null;
            }
            HandlerService handlerService = beanMap.get(type);
            if (handlerService == null) {
                return null;
            }
            return handlerService;
        }
        return null;
    }

    private HandlerService getBean(String name) {
        return getBeanByType(config.getType().get(name));
    }

    @Override
    public String handler(String type) {
        HandlerService handlerService = getBean(type);
        if (handlerService == null) {
            return type + ":没有该类型";
        }
        return handlerService.handler(type);
    }
}
