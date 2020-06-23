package com.geek45.exampleall.aop.demo1;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScannerUtil {
    private static final Logger log = LoggerFactory.getLogger(ScannerUtil.class);
    public static Map<String, Object> scanner(Class annotation, String... basePackage) {
        Map<String, Object> result = new HashMap<>();
        try {
            GenericApplicationContext context = new GenericApplicationContext();
            MyClassPathDefinitonScanner myClassPathDefinitonScanner = new MyClassPathDefinitonScanner(context, annotation);
            // 注册过滤器
            myClassPathDefinitonScanner.registerTypeFilter();
            int beanCount = myClassPathDefinitonScanner.scan(basePackage);
            context.refresh();
            String[] beanDefinitionNames = context.getBeanDefinitionNames();
            List<String> data = new ArrayList<>();
            for (int i = 0; i < beanDefinitionNames.length; i++) {
                if (!beanDefinitionNames[i].startsWith("org.springframework.context")) {
                    data.add(beanDefinitionNames[i]);
                }
            }
            result.put("beanCount", beanCount);
            result.put("beanDefinitionNames", beanDefinitionNames);
            result.put("size", data.size());
            result.put("data", data);
        } catch (Exception e) {
            log.error(JSON.toJSONString(e.getStackTrace()));
        }
        return result;
    }
}
