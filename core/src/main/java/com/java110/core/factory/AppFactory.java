package com.java110.core.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by wuxw on 2017/4/25.
 */

public class AppFactory {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        AppFactory.applicationContext = applicationContext;
    }

    public static Object getBean(Class className){
        return applicationContext.getBean(className);
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
}
