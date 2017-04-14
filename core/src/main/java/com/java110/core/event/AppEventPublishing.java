package com.java110.core.event;

import com.java110.common.log.LoggerEngine;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 事件发布侦听
 *
 * Created by wuxw on 2017/4/14.
 */
public class AppEventPublishing extends LoggerEngine{


    private static Executor taskExecutor;

    /**
     * 保存侦听实例信息，一般启动时加载
     */
    private final static List<AppListener<?>> listeners = new ArrayList<AppListener<?>>();

    /**
     * 根据 事件类型查询侦听
     */
    private final static Map<String,List<AppListener<?>>> cacheListenersMap = new HashMap<String, List<AppListener<?>>>();


    /**
     * 添加 侦听，这个只有启动时，单线程 处理，所以是线程安全的
     * @param listener
     */
    public static void addListenner(AppListener<?> listener){
        listeners.add(listener);
    }

    /**
     * 获取侦听（全部侦听）
     * @return
     */
    public static List<AppListener<?>> getListeners(){
        return listeners;
    }

    /**
     * 根据是否实现了某个接口，返回侦听
     * @param interfaceClassName
     * @return
     */
    public static List<AppListener<?>> getListeners(String interfaceClassName){

        Assert.isNull(interfaceClassName,"获取需要发布的事件处理侦听时，传递事件为空，请检查");

        //先从缓存中获取，为了提升效率
        if(cacheListenersMap.containsKey(interfaceClassName)){
            return cacheListenersMap.get(cacheListenersMap);
        }

        List<AppListener<?>> appListeners = new ArrayList<AppListener<?>>();
        for(AppListener<?> listener : getListeners()){
            Type[] types =  listener.getClass().getGenericInterfaces();
           for (Type type : types) {
               if (type instanceof ParameterizedType) {
                   Type[] typeInterfaces = ((ParameterizedType) type).getActualTypeArguments();
                   for (Type typeInterface : typeInterfaces){
                       if(interfaceClassName.equals(typeInterface.getTypeName())){
                           appListeners.add(listener);
                       }
                   }
               }
           }
        }
        //将数据放入缓存中
        cacheListenersMap.put(interfaceClassName,appListeners);
        return appListeners;
    }


    public static void multicastEvent(String actionTypeCd,String data){
        multicastEvent(new AppCustEvent("",data));
    }


    /**
     * 发布事件
     * @param event
     */
    public static void multicastEvent(final AppEvent event) {
        for (final AppListener<?> listener : getListeners(event.getClass().getName())) {
            Executor executor = getTaskExecutor();
            if (executor != null) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        invokeListener(listener, event);
                    }
                });
            }
            else {
                invokeListener(listener, event);
            }
        }
    }

    /**
     * Return the current task executor for this multicaster.
     */
    protected static Executor getTaskExecutor() {
        return taskExecutor;
    }

    /**
     * Invoke the given listener with the given event.
     * @param listener the ApplicationListener to invoke
     * @param event the current event to propagate
     * @since 4.1
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static void invokeListener(AppListener listener, AppEvent event) {
        try {
            listener.onJava110Event(event);
        }catch (Exception e){
            LoggerEngine.error("发布侦听失败",e);
            throw new RuntimeException("发布侦听失败,"+listener+ event + e);
        }
    }

}
