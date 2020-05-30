package com.java110.core.event.service;

import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.center.DataFlowListenerOrderComparator;

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
public class BusinessServiceDataFlowEventPublishing extends LoggerEngine {

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
    private final static Map<String,List<BusinessServiceDataFlowListener>> cacheListenersMap = new HashMap<String, List<BusinessServiceDataFlowListener>>();

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
     * @param businessTypeCd
     * @since 1.8
     * @return
     */
    public static List<BusinessServiceDataFlowListener> getListeners(String businessTypeCd){

        Assert.hasLength(businessTypeCd,"获取需要发布的事件处理侦听时，传递事件为空，请检查");

        //先从缓存中获取，为了提升效率
        if(cacheListenersMap.containsKey(businessTypeCd)){
            return cacheListenersMap.get(businessTypeCd);
        }

        List<BusinessServiceDataFlowListener> dataFlowListeners = new ArrayList<BusinessServiceDataFlowListener>();
        for(String listenerBeanName : getListeners()){
            BusinessServiceDataFlowListener listener = ApplicationContextFactory.getBean(listenerBeanName,BusinessServiceDataFlowListener.class);
            if(businessTypeCd.equals(listener.getBusinessTypeCd())){
                dataFlowListeners.add(listener);
            }
        }

        //这里排序
        DataFlowListenerOrderComparator.sort(dataFlowListeners);

        //将数据放入缓存中
        cacheListenersMap.put(businessTypeCd,dataFlowListeners);
        return dataFlowListeners;
    }


    /**
     * 发布事件
     * @param dataFlowContext
     */
    public static void multicastEvent(DataFlowContext dataFlowContext) throws BusinessException{
        Assert.notNull(dataFlowContext.getCurrentBusiness(),"当前没有可处理的业务信息！");
        multicastEvent(dataFlowContext.getCurrentBusiness().getBusinessTypeCd(),dataFlowContext,null);
    }


    /**
     * 发布事件
     * @param businessTypeCd
     * @param dataFlowContext
     */
    public static void multicastEvent(String businessTypeCd,DataFlowContext dataFlowContext) throws BusinessException{
        multicastEvent(businessTypeCd,dataFlowContext,null);
    }

    /**
     * 发布事件
     * @param businessTypeCd
     * @param dataFlowContext 这个订单信息，以便于 侦听那边需要用
     */
    public static void multicastEvent(String businessTypeCd,DataFlowContext dataFlowContext,String asyn) throws  BusinessException{
        try {
            BusinessServiceDataFlowEvent targetDataFlowEvent = new BusinessServiceDataFlowEvent(businessTypeCd,dataFlowContext);

            multicastEvent(businessTypeCd,targetDataFlowEvent, asyn);
        }catch (Exception e){
            logger.error("发布侦听失败",e);
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR,e.getMessage());
        }

    }


    /**
     * 发布事件
     * @param event
     * @param asyn A 表示异步处理
     */
    public static void multicastEvent(String businessTypeCd,final BusinessServiceDataFlowEvent event, String asyn) {
        for (final BusinessServiceDataFlowListener listener : getListeners(businessTypeCd)) {

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
    protected static void invokeListener(BusinessServiceDataFlowListener listener, BusinessServiceDataFlowEvent event) {
        try {
            listener.soService(event);
        }catch (Exception e){
            LoggerEngine.error("发布侦听失败",e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
