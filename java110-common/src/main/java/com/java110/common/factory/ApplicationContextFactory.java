package com.java110.common.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * Created by wuxw on 2017/4/25.
 */

public class ApplicationContextFactory {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        ApplicationContextFactory.applicationContext = applicationContext;
    }

    public static Object getBean(Class className){
        return applicationContext.getBean(className);
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(String beanName,Class<T> t){
        Object bean = applicationContext.getBean(beanName);

        if(bean != null && t.isAssignableFrom(bean.getClass()) ){
            return (T)bean;
        }

        return null;
    }
}
