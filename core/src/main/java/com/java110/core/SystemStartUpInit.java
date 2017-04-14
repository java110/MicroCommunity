package com.java110.core;

import com.java110.core.event.AppCustEvent;
import com.java110.core.event.AppEventPublishing;
import com.java110.core.event.AppListener;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


import java.io.IOException;
import java.util.Properties;

/**
 * 系统启动时加载信息
 * Created by wuxw on 2017/4/14.
 */
public class SystemStartUpInit implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * 默认 事件配置路径
     */
    private final String DEFAULT_EVENT_PATH = "classpath:/config/";


    /**
     * 默认 文件名称  .properties
     */
    private final String DEFAULT_FILE_NAME = "event.properties";

    /**
     * 订单调度处理侦听
     */
    private final static String DISPATCH_LISTER = "java110.event.properties.orderDispatchListener";



    /**
     * 加载配置文件，将侦听初始化
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event){


        //加载配置文件，注册订单处理侦听
        try {
            this.load(DEFAULT_EVENT_PATH,DEFAULT_FILE_NAME);
        }
        catch (Exception ex) {
            throw new IllegalStateException("Unable to load configuration files", ex);
        }

    }


    /**
     * 加载文件
     * @param location
     * @param filename
     * @param
     */
    private void load(String location,String filename) throws Exception{
        Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(location+filename));

        String[] listeners = properties.getProperty(DISPATCH_LISTER).split("\\,");

        for(String listener : listeners){

            AppListener<?> appListener = (AppListener<?>)Class.forName(listener).newInstance();

            //将 listener 放入 AppEventPublishing 中方便后期操作
            //注册侦听
            AppEventPublishing.addListenner(appListener);
        }
    }


}
