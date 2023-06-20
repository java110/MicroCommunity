package com.java110.db;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 此类主要用于调优，使用 打印bean的加载时间
 */
@Component
public class BeanInitCostTimeBeanPostProcessor implements BeanPostProcessor {


    private static Map<String, Long> startTime = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        startTime.put(beanName, System.currentTimeMillis());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (Objects.nonNull(startTime.get(beanName))) {
            long time = System.currentTimeMillis() - startTime.get(beanName);
            if(time>1000) {
                System.out.println("beanName=" + beanName + ",costTime=" + time);
            }
        }
        return bean;
    }
}
