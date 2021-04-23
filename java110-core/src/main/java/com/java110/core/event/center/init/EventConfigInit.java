package com.java110.core.event.center.init;

import com.java110.core.event.center.DataFlowEventPublishing;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * 系统启动时加载信息
 * Created by wuxw on 2017/4/14.
 */
public class EventConfigInit {

    /**
     * 默认 事件配置路径classpath:/
     */
    private final static String DEFAULT_EVENT_PATH = "config/";


    /**
     * 默认 文件名称  .properties
     */
    private final static String DEFAULT_FILE_NAME = "center_event.properties";

    /**
     * 订单调度处理侦听
     */
    private final static String DATAFLOW_LISTENER = "java110.event.properties.centerServiceListener";



    public static void initSystemConfig(){
        //加载配置文件，注册订单处理侦听
        try {

            Properties properties = load(DEFAULT_EVENT_PATH,DEFAULT_FILE_NAME);

            registerListener(properties);

        }
        catch (Exception ex) {
            throw new IllegalStateException("system init error", ex);
        }
    }


    /**
     * 加载文件
     * @param location
     * @param filename
     * @param
     */
    private  static Properties load(String location,String filename) throws Exception{
        Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(location+filename));
        return properties;
    }

    /**
     * 注册侦听
     * @param properties
     */
    private static void registerListener(Properties properties) throws Exception{

        String[] listeners = properties.getProperty(DATAFLOW_LISTENER).split("\\,");

        for(String listener : listeners){
            //将 listener 放入 AppEventPublishing 中方便后期操作
            //注册侦听
            DataFlowEventPublishing.addListener(listener);
        }
    }




}
