package com.java110.event.center;

import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.BusinessException;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.core.context.DataFlow;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 数据流 事件发布
 * Created by wuxw on 2018/4/17.
 */
public class DataFlowEventPublishing extends LoggerEngine {

    private static Executor taskExecutor;

    //默认 线程数 100
    private final static int DEFAULT_THREAD_NUM = 100;

    /**
     * 保存侦听实例信息，一般启动时加载
     */
    private final static List<DataFlowListener<?>> listeners = new ArrayList<DataFlowListener<?>>();

    /**
     * 保存事件实例信息，一般启动时加载
     */
    private final static Map<String,Class<DataFlowEvent>> events = new HashMap<String,Class<DataFlowEvent>>();

    /**
     * 根据 事件类型查询侦听
     */
    private final static Map<String,List<DataFlowListener<?>>> cacheListenersMap = new HashMap<String, List<DataFlowListener<?>>>();





    /**
     * 添加 侦听，这个只有启动时，单线程 处理，所以是线程安全的
     * @param listener
     */
    public static void addListenner(DataFlowListener<?> listener){
        listeners.add(listener);
    }

    /**
     * 获取侦听（全部侦听）
     * @return
     */
    public static List<DataFlowListener<?>> getListeners(){
        return listeners;
    }

    /**
     * 根据是否实现了某个接口，返回侦听
     * @param interfaceClassName
     * @since 1.8
     * @return
     */
    public static List<DataFlowListener<?>> getListeners(String interfaceClassName){

        Assert.isNull(interfaceClassName,"获取需要发布的事件处理侦听时，传递事件为空，请检查");

        //先从缓存中获取，为了提升效率
        if(cacheListenersMap.containsKey(interfaceClassName)){
            return cacheListenersMap.get(interfaceClassName);
        }

        List<DataFlowListener<?>> dataFlowListeners = new ArrayList<DataFlowListener<?>>();
        for(DataFlowListener<?> listener : getListeners()){
            Type[] types =  listener.getClass().getGenericInterfaces();
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    Type[] typeInterfaces = ((ParameterizedType) type).getActualTypeArguments();
                    for (Type typeInterface : typeInterfaces){
                        if(interfaceClassName.equals(typeInterface.getTypeName())){
                            dataFlowListeners.add(listener);
                        }
                    }
                }
            }
        }

        //这里排序
        DataFlowListenerOrderComparator.sort(dataFlowListeners);

        //将数据放入缓存中
        cacheListenersMap.put(interfaceClassName,dataFlowListeners);
        return dataFlowListeners;
    }

    /**
     * 注册事件
     */
    public static void addEvent(String serviceCode ,Class<DataFlowEvent> event) {
        events.put(serviceCode,event);
    }

    /**
     * 获取事件
     * @param serviceCode
     * @return
     * @throws Exception
     */
    public static Class<DataFlowEvent> getEvent(String serviceCode) throws BusinessException{
        Class<DataFlowEvent> targetEvent = events.get(serviceCode);
        //Assert.notNull(targetEvent,"改服务未注册该事件[serviceCode = "+serviceCode+"]，系统目前不支持!");
        return targetEvent;
    }


    /**
     * 发布事件
     * @param serviceCode
     * @param dataFlow
     */
    public static void multicastEvent(String serviceCode,DataFlow dataFlow) throws BusinessException{
        multicastEvent(serviceCode,dataFlow,null);
    }

    /**
     * 发布事件
     * @param serviceCode
     * @param dataFlow 这个订单信息，以便于 侦听那边需要用
     */
    public static void multicastEvent(String serviceCode,DataFlow dataFlow,String asyn) throws  BusinessException{
        try {
            Class<DataFlowEvent> dataFlowEventClass = getEvent(serviceCode);

            if(dataFlowEventClass == null){
                return ;
            }

            Class[] parameterTypes = {Object.class, DataFlow.class};

            Constructor constructor = dataFlowEventClass.getClass().getConstructor(parameterTypes);
            Object[] parameters = {null, dataFlow};
            DataFlowEvent targetDataFlowEvent = (DataFlowEvent) constructor.newInstance(parameters);
            multicastEvent(targetDataFlowEvent, asyn);
        }catch (Exception e){
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR,"发布侦听失败，失败原因为："+e);
        }

    }




    /**
     * 发布事件
     * @param event
     * @param asyn A 表示异步处理
     */
    public static void multicastEvent(final DataFlowEvent event, String asyn) {
        for (final DataFlowListener<?> listener : getListeners(event.getClass().getName())) {

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
    protected static void invokeListener(DataFlowListener listener, DataFlowEvent event) {
        try {
            listener.soService(event);
        }catch (Exception e){
            LoggerEngine.error("发布侦听失败",e);
            throw new RuntimeException("发布侦听失败,"+listener+ event + e);
        }
    }
}
