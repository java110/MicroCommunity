package com.java110.core.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2017/4/25.
 */
@Configuration
public class AppFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getBean(Class className){
        return applicationContext.getBean(className);
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
}
