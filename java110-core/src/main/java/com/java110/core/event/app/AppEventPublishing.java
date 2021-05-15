package com.java110.core.event.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.Assert;
import com.java110.core.context.AppContext;
import com.java110.entity.order.BusiOrder;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 事件发布侦听
 *
 * Created by wuxw on 2017/4/14.
 */
public class AppEventPublishing extends LoggerEngine{


    private static Executor taskExecutor;

    //默认 线程数 100
    private final static int DEFAULT_THREAD_NUM = 100;

    /**
     * 保存侦听实例信息，一般启动时加载
     */
    private final static List<AppListener<?>> listeners = new ArrayList<AppListener<?>>();

    /**
     * 保存事件实例信息，一般启动时加载
     */
    private final static Map<String,Class<AppEvent>> events = new HashMap<String,Class<AppEvent>>();

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
     * @since 1.8
     * @return
     */
    public static List<AppListener<?>> getListeners(String interfaceClassName){

        Assert.isNull(interfaceClassName,"获取需要发布的事件处理侦听时，传递事件为空，请检查");

        //先从缓存中获取，为了提升效率
        if(cacheListenersMap.containsKey(interfaceClassName)){
            return cacheListenersMap.get(interfaceClassName);
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

    /**
     * 注册事件
     */
    public static void addEvent(String actionTypeCd ,Class<AppEvent> event) {
        events.put(actionTypeCd,event);
    }

    /**
     * 获取事件
     * @param actionTypeCd
     * @return
     * @throws Exception
     */
    public static Class<AppEvent> getEvent(String actionTypeCd) throws Exception{
        Class<AppEvent> targetEvent = events.get(actionTypeCd);
        Assert.notNull(targetEvent,"未注册该事件[actionTypeCd = "+actionTypeCd+"]，系统目前不支持!");
        return targetEvent;
    }


    /**
     * 发布事件
     * @param actionTypeCd
     * @param data
     */
    public static void multicastEvent(String actionTypeCd,String data) throws  Exception{
        multicastEvent(actionTypeCd,null,data,null);
    }

    /**
     * 发布事件
     * @param actionTypeCd
     * @param data
     */
    public static void multicastEvent(String actionTypeCd,String data,String asyn) throws  Exception{
        multicastEvent(actionTypeCd,null,data,asyn);
    }

    /**
     * 发布事件
     * @param actionTypeCd
     * @param orderInfo 这个订单信息，以便于 侦听那边需要用
     * @param data 对应信息，侦听，一般需要处理这个就可以
     */
    public static void multicastEvent(String actionTypeCd,String orderInfo,String data,String asyn) throws  Exception{
        Class<AppEvent> appEvent = getEvent(actionTypeCd);

        Class<?>[] parameterTypes={Object.class,String.class};

        Constructor<?> constructor = appEvent.getClass().getConstructor(parameterTypes);
        Object[] parameters={orderInfo,data};
        AppEvent targetAppEvent = (AppEvent)constructor.newInstance(parameters);
        multicastEvent(targetAppEvent,asyn);

    }

    /**
     * 一次发布同一个动作的 订单数据，避免多次调用子服务，印象性能
     * @param context 上下文对象
     * @param data 封装了 data节点 的数据
     * @throws Exception
     */
    public static void multicastEvent(AppContext context, Map<String,JSONArray> data, String asyn) throws Exception{
        Assert.hasSize(data,"订单调度时，没有可处理的数据，data="+data);

       Set<String> keys =  data.keySet();

       for(String key : keys){
           Class<AppEvent> appEvent = getEvent(key);

           Class<?>[] parameterTypes={Object.class,AppContext.class,JSONArray.class};

           Constructor<?> constructor = appEvent.getClass().getConstructor(parameterTypes);
           context.setBo_action_type(key);
           Object[] parameters={null,context,data.get(key)};
           AppEvent targetAppEvent = (AppEvent)constructor.newInstance(parameters);

           multicastEvent(targetAppEvent,asyn);
       }


    }


    /**
     * 发布事件
     * @param event
     * @param asyn A 表示异步处理
     */
    public static void multicastEvent(final AppEvent event,String asyn) {
        for (final AppListener<?> listener : getListeners(event.getClass().getName())) {

            if(CommonConstant.PROCESS_ORDER_ASYNCHRONOUS.equals(asyn)){ //异步处理

                Executor executor = getTaskExecutor();
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
    protected static synchronized Executor getTaskExecutor() {
        if(taskExecutor == null) {
            taskExecutor = Executors.newFixedThreadPool(DEFAULT_THREAD_NUM);
        }
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
            listener.soDataService(event);
        }catch (Exception e){
            LoggerEngine.error("发布侦听失败",e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Invoke the given listener with the given event.
     * @param listener the ApplicationListener to invoke
     * @param event the current event to propagate
     * @since 4.1
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static JSONObject invokeQueryData(AppListener listener, AppEvent event) {
           return listener.queryDataInfo(event);
    }

    /**
     * Invoke the given listener with the given event.
     * @param listener the ApplicationListener to invoke
     * @param event the current event to propagate
     * @since 4.1
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static JSONObject invokeQueryNeedDeleteData(AppListener listener, AppEvent event) {
        return listener.queryNeedDeleteDataInfo(event);
    }

    /**
     * 查询数据事件
     * @param context 上下文
     * @param busiOrder 订单项
     * @return
     */
    public static JSONObject queryDataInfoEvent(AppContext context, BusiOrder busiOrder) throws Exception{

        Class<AppEvent> appEvent = getEvent(busiOrder.getActionTypeCd());

        Class<?>[] parameterTypes={Object.class,AppContext.class};

        Constructor<?> constructor = appEvent.getClass().getConstructor(parameterTypes);

        Object[] parameters={null,context};

        AppEvent targetAppEvent = (AppEvent)constructor.newInstance(parameters);
        JSONObject queryDataJson = new JSONObject();
        for (AppListener<?> listener : getListeners(targetAppEvent.getClass().getName())) {
            queryDataJson.putAll(invokeQueryData(listener,targetAppEvent));
        }

        return queryDataJson;
    }


    /**
     * 查询数据事件
     * @param context 上下文
     * @param busiOrder 订单项
     * @return
     */
    public static JSONObject queryNeedDeleteDataInfoEvent(AppContext context, BusiOrder busiOrder) throws Exception{

        Class<AppEvent> appEvent = getEvent(busiOrder.getActionTypeCd());

        Class<?>[] parameterTypes={Object.class,AppContext.class};

        Constructor<?> constructor = appEvent.getClass().getConstructor(parameterTypes);

        Object[] parameters={null,context};

        AppEvent targetAppEvent = (AppEvent)constructor.newInstance(parameters);
        JSONObject queryDataJson = new JSONObject();
        for (AppListener<?> listener : getListeners(targetAppEvent.getClass().getName())) {
            queryDataJson.putAll(invokeQueryNeedDeleteData(listener,targetAppEvent));
        }

        return queryDataJson;
    }

}
