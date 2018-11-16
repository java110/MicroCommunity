package com.java110.event.service.api;

import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.BusinessException;
import com.java110.common.factory.ApplicationContextFactory;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.center.DataFlowListenerOrderComparator;
import com.java110.event.service.BusinessServiceDataFlowEvent;
import com.java110.event.service.BusinessServiceDataFlowListener;

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
public class ServiceDataFlowEventPublishing extends LoggerEngine {

    private static Executor taskExecutor;

    //默认 线程数 100
    private final static int DEFAULT_THREAD_NUM = 100;

    /**
     * 保存侦听实例信息，一般启动时加载
     */
    private final static List<String> listeners = new ArrayList<String>();

    /**
     * 根据 事件类型查询侦听
     */
    private final static Map<String,List<ServiceDataFlowListener>> cacheListenersMap = new HashMap<String, List<ServiceDataFlowListener>>();

    /**
     * 添加 侦听，这个只有启动时，单线程 处理，所以是线程安全的
     * @param listener
     */
    public static void addListener(String listener){
        listeners.add(listener);
    }

    /**
     * 获取侦听（全部侦听）
     * @return
     */
    public static List<String> getListeners(){
        return listeners;
    }

    /**
     * 根据是否实现了某个接口，返回侦听
     * @param serviceCode
     * @since 1.8
     * @return
     */
    public static List<ServiceDataFlowListener> getListeners(String serviceCode){

        Assert.hasLength(serviceCode,"获取需要发布的事件处理侦听时，传递事件为空，请检查");

        //先从缓存中获取，为了提升效率
        if(cacheListenersMap.containsKey(serviceCode)){
            return cacheListenersMap.get(serviceCode);
        }

        List<ServiceDataFlowListener> dataFlowListeners = new ArrayList<ServiceDataFlowListener>();
        for(String listenerBeanName : getListeners()){
            ServiceDataFlowListener listener = ApplicationContextFactory.getBean(listenerBeanName,ServiceDataFlowListener.class);
            if(serviceCode.equals(listener.getServiceCode())){
                dataFlowListeners.add(listener);
            }
        }

        //这里排序
        DataFlowListenerOrderComparator.sort(dataFlowListeners);

        //将数据放入缓存中
        cacheListenersMap.put(serviceCode,dataFlowListeners);
        return dataFlowListeners;
    }


    /**
     * 发布事件
     * @param dataFlowContext
     */
    public static void multicastEvent(DataFlowContext dataFlowContext,AppService appService) throws BusinessException{
        Assert.notNull(dataFlowContext.getServiceCode(),"当前没有可处理的业务信息！");
        multicastEvent(dataFlowContext.getServiceCode(),dataFlowContext,appService,null);
    }


    /**
     * 发布事件
     * @param serviceCode
     * @param dataFlowContext
     */
    public static void multicastEvent(String serviceCode,DataFlowContext dataFlowContext,AppService appService) throws BusinessException{
        multicastEvent(serviceCode,dataFlowContext,appService,null);
    }

    /**
     * 发布事件
     * @param serviceCode
     * @param dataFlowContext 这个订单信息，以便于 侦听那边需要用
     */
    public static void multicastEvent(String serviceCode, DataFlowContext dataFlowContext, AppService appService, String asyn) throws  BusinessException{
        try {
            ServiceDataFlowEvent targetDataFlowEvent = new ServiceDataFlowEvent(serviceCode,dataFlowContext,appService);

            multicastEvent(serviceCode,targetDataFlowEvent, asyn);
        }catch (Exception e){
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR,"发布侦听失败，失败原因为："+e);
        }

    }


    /**
     * 发布事件
     * @param event
     * @param asyn A 表示异步处理
     */
    public static void multicastEvent(String serviceCode,final ServiceDataFlowEvent event, String asyn) {
        for (final ServiceDataFlowListener listener : getListeners(serviceCode)) {

            if(CommonConstant.PROCESS_ORDER_ASYNCHRONOUS.equals(asyn)){ //异步处理

                Executor executor = getTaskExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        invokeListener(listener, event);
                    }
                });
                break;
            }
            else {
                invokeListener(listener, event);
                break;
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
    protected static void invokeListener(ServiceDataFlowListener listener, ServiceDataFlowEvent event) {
        try {
            listener.soService(event);
        }catch (Exception e){
            LoggerEngine.error("发布侦听失败",e);
            throw new RuntimeException("发布侦听失败,"+listener+ event + e);
        }
    }
}
