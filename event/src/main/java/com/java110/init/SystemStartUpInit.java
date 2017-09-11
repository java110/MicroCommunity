package com.java110.init;

import com.java110.core.factory.AppFactory;
import com.java110.event.AppEventPublishing;
import com.java110.event.AppListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.naming.ConfigurationException;
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
     * 订单调度事件
     */
    private final static String DISPATCH_EVENT = "java110.event.properties.orderDispatchEvent";



    /**
     * 加载配置文件，将侦听初始化
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event){


        //加载配置文件，注册订单处理侦听
        try {
            Properties properties = this.load(DEFAULT_EVENT_PATH,DEFAULT_FILE_NAME);
            registerListener(properties);

            //注册事件
            registerEvent(properties);


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
    private Properties load(String location,String filename) throws Exception{
        Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(location+filename));
        return properties;
    }

    /**
     * 注册侦听
     * @param properties
     */
    private void registerListener(Properties properties) throws Exception{

        String[] listeners = properties.getProperty(DISPATCH_LISTER).split("\\,");

        for(String listener : listeners){

            //这里不能直接反射，这样 IXXXService 无法注入，所以直接从spring 中获取已经注入的
            //AppListener<?> appListener = (AppListener<?>)Class.forName(listener).newInstance();

            AppListener<?> appListener = (AppListener<?>)AppFactory.getBean(Class.forName(listener));

            //将 listener 放入 AppEventPublishing 中方便后期操作
            //注册侦听
            AppEventPublishing.addListenner(appListener);
        }
    }

    /**
     * 注册事件
     * @param properties
     * @throws Exception
     */
    private void registerEvent(Properties properties) throws Exception{
        String[] events = properties.getProperty(DISPATCH_EVENT).split("\\,");

        for (String event : events){

            if(StringUtils.isBlank(event) || !event.contains("::")){
                throw new ConfigurationException("配置错误，["+DISPATCH_EVENT+"= "+events+"] 当前 [event = "+event+"],不存在 :: ,配置格式为 A::B");
            }

            String[] tmpEvent = event.split("::");

            if(tmpEvent.length > 2){
                throw new ConfigurationException("配置错误，["+DISPATCH_EVENT+"= "+events+"] 当前 [event = "+event+"],只能有一个 :: ,配置格式为 A::B");
            }

            Class clazz = Class.forName(tmpEvent[1]);

            AppEventPublishing.addEvent(tmpEvent[0],clazz);
        }

    }


}
