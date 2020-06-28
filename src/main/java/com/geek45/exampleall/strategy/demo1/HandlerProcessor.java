package com.geek45.exampleall.strategy.demo1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
@Lazy
public class HandlerProcessor implements BeanFactoryPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(HandlerProcessor.class);

    private static final String HANDLER_PACKAGE = "com.geek45.exampleall*";

    public static String test = "1";

    /**
     * 扫描@HandlerType ，  初始化HandlerContext， 将其注册到spring容器
     * @param configurableListableBeanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        log.info(test);
        List<String> handlerList = Lists.newArrayListWithExpectedSize(3);
        //TODO 扫描指定的包，获取类上面的注解 根据注解不同，将不同的类放到map中
        Map<String, Object> result = ScannerUtil.scanner(HandlerType.class, HANDLER_PACKAGE);
        int size = Integer.valueOf(result.get("size").toString());
        log.info("扫描到{}个包", size);
        List<String> data = (List<String>) result.get("data");
        for (String beanName : data) {
            handlerList.add(beanName);
        }
        log.info("增加{}个handler", handlerList.size());
        HandlerContext handlerContext = new HandlerContext(handlerList);
        configurableListableBeanFactory.registerSingleton(HandlerContext.class.getName(), handlerContext);
    }
}
