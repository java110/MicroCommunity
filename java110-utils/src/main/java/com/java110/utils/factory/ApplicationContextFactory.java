package com.java110.utils.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.Locale;

/**
 * Created by wuxw on 2017/4/25.
 */

public class ApplicationContextFactory {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        ApplicationContextFactory.applicationContext = applicationContext;
    }

    public static Object getBean(Class<?> className){
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

    /**
     * 获取应用名称
     * @return
     */
    public static String getApplicationName(){
        return applicationContext.getEnvironment().getProperty("spring.application.name");
    }


    // 国际化使用
    public static String getMessage(String key) {
        return applicationContext.getMessage(key, null, Locale.getDefault());
    }


    /// 获取当前环境
    public static String getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }


}
