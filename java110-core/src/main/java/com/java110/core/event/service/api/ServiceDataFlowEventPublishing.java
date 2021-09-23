package com.java110.core.event.service.api;

import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.core.event.center.DataFlowListenerOrderComparator;
import org.springframework.http.HttpMethod;

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
    private static final int DEFAULT_THREAD_NUM = 100;

    /**
     * 保存侦听实例信息，一般启动时加载
     */
    private static final List<String> listeners = new ArrayList<String>();

    /**
     * 根据 事件类型查询侦听
     */
    private static final Map<String, List<ServiceDataFlowListener>> cacheListenersMap = new HashMap<String, List<ServiceDataFlowListener>>();

    /**
     * 添加 侦听，这个只有启动时，单线程 处理，所以是线程安全的
     *
     * @param listener
     */
    public static void addListener(String listener) {
        listeners.add(listener);
    }

    /**
     * 获取侦听（全部侦听）
     *
     * @return
     */
    public static List<String> getListeners() {
        return listeners;
    }

    /**
     * 根据是否实现了某个接口，返回侦听
     *
     * @param serviceCode
     * @return
     * @since 1.8
     */
    public static List<ServiceDataFlowListener> getListeners(String serviceCode, String httpMethod) {

        Assert.hasLength(serviceCode, "获取需要发布的事件处理侦听时，传递事件为空，请检查");

        String needCachedServiceCode = serviceCode + httpMethod;
        //先从缓存中获取，为了提升效率
        if (cacheListenersMap.containsKey(needCachedServiceCode)) {
            return cacheListenersMap.get(needCachedServiceCode);
        }

        List<ServiceDataFlowListener> dataFlowListeners = new ArrayList<ServiceDataFlowListener>();
        for (String listenerBeanName : getListeners()) {
            ServiceDataFlowListener listener = ApplicationContextFactory.getBean(listenerBeanName, ServiceDataFlowListener.class);
            if (serviceCode.equals(listener.getServiceCode())
                    && listener.getHttpMethod() == HttpMethod.valueOf(httpMethod)) {
                dataFlowListeners.add(listener);
            }
            //特殊处理 透传类接口
            if (ServiceCodeConstant.SERVICE_CODE_DO_SERVICE_TRANSFER.equals(listener.getServiceCode())
                    && ServiceCodeConstant.SERVICE_CODE_DO_SERVICE_TRANSFER.equals(serviceCode)) {
                dataFlowListeners.add(listener);
            }
            //特殊处理 透传类接口
            if (ServiceCodeConstant.SERVICE_CODE_SYSTEM_TRANSFER.equals(listener.getServiceCode())
                    && ServiceCodeConstant.SERVICE_CODE_SYSTEM_TRANSFER.equals(serviceCode)) {
                dataFlowListeners.add(listener);
            }

            //特殊处理 透传类接口
            if (ServiceCodeConstant.SERVICE_CODE_SYSTEM_CMD.equals(listener.getServiceCode())
                    && ServiceCodeConstant.SERVICE_CODE_SYSTEM_CMD.equals(serviceCode)) {
                dataFlowListeners.add(listener);
            }
        }

        //这里排序
        DataFlowListenerOrderComparator.sort(dataFlowListeners);


        //将数据放入缓存中
        if (dataFlowListeners.size() > 0) {
            cacheListenersMap.put(needCachedServiceCode, dataFlowListeners);
        }
        return dataFlowListeners;
    }


    /**
     * 发布事件
     *
     * @param dataFlowContext
     */
    public static void multicastEvent(DataFlowContext dataFlowContext, AppService appService) throws BusinessException {
        Assert.notNull(dataFlowContext.getServiceCode(), "当前没有可处理的业务信息！");
        multicastEvent(dataFlowContext.getServiceCode(), dataFlowContext, appService, null);
    }


    /**
     * 发布事件
     *
     * @param serviceCode
     * @param dataFlowContext
     */
    public static void multicastEvent(String serviceCode, DataFlowContext dataFlowContext, AppService appService) throws BusinessException {
        multicastEvent(serviceCode, dataFlowContext, appService, null);
    }

    /**
     * 发布事件
     *
     * @param serviceCode
     * @param dataFlowContext 这个订单信息，以便于 侦听那边需要用
     */
    public static void multicastEvent(String serviceCode, DataFlowContext dataFlowContext, AppService appService, String asyn) throws BusinessException {
        try {
            ServiceDataFlowEvent targetDataFlowEvent = new ServiceDataFlowEvent(serviceCode, dataFlowContext, appService);

            multicastEvent(serviceCode, targetDataFlowEvent, asyn);
        } catch (Exception e) {
            logger.error("发布侦听失败，失败原因为：", e);
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, e.getMessage());
        }

    }


    /**
     * 发布事件
     *
     * @param event
     * @param asyn  A 表示异步处理
     */
    public static void multicastEvent(String serviceCode, final ServiceDataFlowEvent event, String asyn) {
        String httpMethod = event.getDataFlowContext().getRequestCurrentHeaders().get(CommonConstant.HTTP_METHOD);
        List<ServiceDataFlowListener> listeners = getListeners(serviceCode, httpMethod);
        //这里判断 serviceCode + httpMethod 的侦听，如果没有注册直接报错。
        if (listeners == null || listeners.size() == 0) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR,
                    "服务【" + serviceCode + "】调用方式【" + httpMethod + "】当前不支持");
        }
        for (final ServiceDataFlowListener listener : listeners) {

            if (CommonConstant.PROCESS_ORDER_ASYNCHRONOUS.equals(asyn)) { //异步处理

                Executor executor = getTaskExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        invokeListener(listener, event);
                    }
                });
                break;
            } else {
                invokeListener(listener, event);
                break;
            }
        }
    }


    /**
     * Return the current task executor for this multicaster.
     */
    protected static synchronized Executor getTaskExecutor() {
        if (taskExecutor == null) {
            taskExecutor = Executors.newFixedThreadPool(DEFAULT_THREAD_NUM);
        }
        return taskExecutor;
    }

    /**
     * Invoke the given listener with the given event.
     *
     * @param listener the ApplicationListener to invoke
     * @param event    the current event to propagate
     * @since 4.1
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static void invokeListener(ServiceDataFlowListener listener, ServiceDataFlowEvent event) {
        try {
            listener.soService(event);
        } catch (Exception e) {
            LoggerEngine.error("发布侦听失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
