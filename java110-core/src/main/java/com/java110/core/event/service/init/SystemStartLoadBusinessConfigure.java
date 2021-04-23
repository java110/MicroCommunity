package com.java110.core.event.service.init;

import com.java110.utils.util.Assert;
import com.java110.core.event.service.BusinessServiceDataFlowEventPublishing;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * 系统启动时加载信息
 * Created by wuxw on 2017/4/14.
 */
public class SystemStartLoadBusinessConfigure {

    /**
     * 默认 事件配置路径classpath:/
     */
    private final static String DEFAULT_EVENT_PATH = "config/";

    /**
     * 默认 文件名称  .properties
     */
    private final static String DEFAULT_FILE_NAME = "business_listener.properties";


    public static void initSystemConfig(String listenerPath){
        //加载配置文件，注册订单处理侦听
        try {
            Assert.hasLength(listenerPath,"listenerPath 为空");
            Properties properties = load(DEFAULT_EVENT_PATH,DEFAULT_FILE_NAME);

            registerListener(listenerPath,properties);

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
    private static  Properties load(String location,String filename) throws Exception{
        Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(location+filename));
        return properties;
    }

    /**
     * 注册侦听
     * @param properties
     */
    private static void registerListener(String listenerPath,Properties properties) throws Exception{

        String[] listeners = properties.getProperty(listenerPath).split("\\,");

        for(String listener : listeners){

            //这里不能直接反射，这样 IXXXService 无法注入，所以直接从spring 中获取已经注入的
            //AppListener<?> appListener = (AppListener<?>)Class.forName(listener).newInstance();
            try {
               // BusinessServiceDataFlowListener businessServiceDataFlowListener = (BusinessServiceDataFlowListener) ApplicationContextFactory.getBean(listener);
                //将 listener 放入 AppEventPublishing 中方便后期操作
                //注册侦听
                BusinessServiceDataFlowEventPublishing.addListener(listener);
            }catch (Exception e){

            }
        }
    }



}
