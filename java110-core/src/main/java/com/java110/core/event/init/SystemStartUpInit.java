package com.java110.core.event.init;

import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.core.event.app.AppEvent;
import com.java110.core.event.app.AppEventPublishing;
import com.java110.core.event.app.AppListener;
import com.java110.core.event.listener.common.CommonDispatchListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.naming.ConfigurationException;
import java.util.Properties;

/**
 * 系统启动时加载信息
 * Created by wuxw on 2017/4/14.
 */
public class SystemStartUpInit /*implements ApplicationListener<ApplicationReadyEvent>*/ {

    /**
     * 默认 事件配置路径classpath:/
     */
    private final String DEFAULT_EVENT_PATH = "config/";


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
     * 服务
     */
    private final static String LISTENER_SERVICE = "java110.event.properties.listener.service";



    /**
     * 加载配置文件，将侦听初始化
     * @param event
     */
   // @Override
    public void onApplicationEvent(ApplicationReadyEvent event){


        //加载配置文件，注册订单处理侦听
        try {

            ApplicationContextFactory.setApplicationContext(event.getApplicationContext());

            Properties properties = this.load(DEFAULT_EVENT_PATH,DEFAULT_FILE_NAME);

            registerListener(properties);

            //注册事件
            registerEvent(properties);

            //注册服务
            registerService(properties);
        }
        catch (Exception ex) {
            throw new IllegalStateException("system init error", ex);
        }

    }

    public void initSystemConfig(ApplicationContext context){
        //加载配置文件，注册订单处理侦听
        try {

            ApplicationContextFactory.setApplicationContext(context);

            Properties properties = this.load(DEFAULT_EVENT_PATH,DEFAULT_FILE_NAME);

            registerListener(properties);

            //注册事件
            registerEvent(properties);

            //注册服务
            registerService(properties);
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

            AppListener<?> appListener = (AppListener<?>) ApplicationContextFactory.getBean(listener);

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

            Class<AppEvent> clazz = (Class<AppEvent>) Class.forName(tmpEvent[1]);

            AppEventPublishing.addEvent(tmpEvent[0],clazz);
        }

    }


    /**
     * 注册服务
     * @param properties
     * @throws Exception
     */
    private void registerService(Properties properties) throws Exception{
        String[] services = properties.getProperty(LISTENER_SERVICE).split("\\,");

        for(String service : services){
            if(StringUtils.isBlank(service) || !service.contains("::")){
                throw new ConfigurationException("配置错误，["+LISTENER_SERVICE+"= "+services+"] 当前 [event = "+service+"],不存在 :: ,配置格式为 A::B");
            }

            String[] tmpService = service.split("::");

            if(tmpService.length > 2){
                throw new ConfigurationException("配置错误，["+LISTENER_SERVICE+"= "+services+"] 当前 [event = "+service+"],只能有一个 :: ,配置格式为 A::B");
            }

            CommonDispatchListener.addService(tmpService[0],tmpService[1]);

        }

    }


}
