package com.java110.core.event.center;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.Assert;
import com.java110.core.context.IOrderDataFlowContext;
import com.java110.entity.order.Business;
import com.java110.core.event.center.event.*;
import com.java110.core.event.center.listener.DataFlowListener;

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
    private final static List<String> listeners = new ArrayList<String>();

    /**
     * 保存事件实例信息，一般启动时加载
     */
   /* private final static Map<String,Class<DataFlowEvent>> events = new HashMap<String,Class<DataFlowEvent>>();*/

    /**
     * 根据 事件类型查询侦听
     */
    private final static Map<String,List<DataFlowListener<?>>> cacheListenersMap = new HashMap<String, List<DataFlowListener<?>>>();





    /**
     * 添加 侦听，这个只有启动时，单线程 处理，所以是线程安全的
     * @param listener
     */
    /*public static void addListener(DataFlowListener<?> listener){
        listeners.add(listener);
    }*/

    /**
     * 注解注册侦听
     * @param listenerBeanName
     */
    public static void addListener(String listenerBeanName){
        //将 listener 放入 AppEventPublishing 中方便后期操作
        //注册侦听
        listeners.add(listenerBeanName);
    }

    /**
     * 获取侦听（全部侦听）
     * @return
     */
    private static List<String> getListeners(){
        return listeners;
    }

    /**
     * 根据是否实现了某个接口，返回侦听
     * @param interfaceClassName
     * @since 1.8
     * @return
     */
    private static List<DataFlowListener<?>> getListeners(String interfaceClassName){

        Assert.hasLength(interfaceClassName,"获取需要发布的事件处理侦听时，传递事件为空，请检查");

        //先从缓存中获取，为了提升效率
        if(cacheListenersMap.containsKey(interfaceClassName)){
            return cacheListenersMap.get(interfaceClassName);
        }

        List<DataFlowListener<?>> dataFlowListeners = new ArrayList<DataFlowListener<?>>();

        for(String listenerBeanName : getListeners()){
            DataFlowListener<?> listener = ApplicationContextFactory.getBean(listenerBeanName,DataFlowListener.class);
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
    /*public static void addEvent(String serviceCode ,Class<DataFlowEvent> event) {
        events.put(serviceCode,event);
    }*/

    /**
     * 获取事件
     * @param serviceCode
     * @return
     * @throws Exception
     */
    /*public static Class<DataFlowEvent> getEvent(String serviceCode) throws BusinessException{
        Class<DataFlowEvent> targetEvent = events.get(serviceCode);
        //Assert.notNull(targetEvent,"改服务未注册该事件[serviceCode = "+serviceCode+"]，系统目前不支持!");
        return targetEvent;
    }*/


    /**
     * 发布事件
     * @param event 事件
     */
   /* public static void multicastEvent(String serviceCode,DataFlow dataFlow) throws BusinessException{
        multicastEvent(serviceCode,dataFlow,null);
    }*/

    private static void multicastEvent(final DataFlowEvent event) throws BusinessException{
        multicastEvent(event,"S");
    }

    /**
     * 发布事件
     * @param event
     * @param asyn A 表示异步处理
     */
    private static void multicastEvent(final DataFlowEvent event, String asyn) {

            for (final DataFlowListener<?> listener :  getListeners(event.getClass().getName())) {

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
            throw new RuntimeException(e.getMessage());
        }
    }


    /***********************************************发布侦听 开始***************************************************************/
    /**
     * 发布接受请求事件
     * @param requestData
     * @param headers
     */
    public static void receiveRequest(String requestData,Map<String,String> headers){
        multicastEvent(new ReceiveRequestEvent("",null,requestData,headers));
    }

    /**
     * 发布预校验侦听
     * @param requestData
     * @param headers
     */
    public static void preValidateData(String requestData,Map<String,String> headers){
        multicastEvent(new DataPreValidateEvent("",null,requestData,headers));
    }

    /**
     * 初始化 DataFlow 对象完成
     * @param dataFlow 数据流对象
     */
    public static void initDataFlowComplete(IOrderDataFlowContext dataFlow){
        multicastEvent(new DataFlowInitCompleteEvent("",dataFlow));
    }

    /**
     * 规则校验完成事件
     * @param dataFlow 数据流对象
     */
    public static void ruleValidateComplete(IOrderDataFlowContext dataFlow){
        multicastEvent(new RuleValidateCompleteEvent("",dataFlow));
    }

    /**
     * 加载配置文件完成
     * @param dataFlow 数据流对象
     */
    public static void loadConfigDataComplete(IOrderDataFlowContext dataFlow){
        multicastEvent(new LoadConfigDataCompleteEvent("",dataFlow));
    }


    /**
     * 调用业务系统事件
     * @param dataFlow 数据流
     */
    public static void invokeBusinessSystem(IOrderDataFlowContext dataFlow){
        multicastEvent(new InvokeBusinessSystemEvent("",dataFlow));
    }

    /**
     * 调用完成业务系统事件
     * @param dataFlow 数据流
     */
    public static void invokeFinishBusinessSystem(IOrderDataFlowContext dataFlow){
        multicastEvent(new InvokeFinishBusinessSystemEvent("",dataFlow));
    }

    /**
     * 调用 确认订单完成
     * @param dataFlow
     */
    public static void invokeConfirmFinishBusinessSystem(IOrderDataFlowContext dataFlow) {
        multicastEvent(new InvokeConfirmFinishBusinessSystemEvent("",dataFlow));
    }


    /**
     * 调用业务系统成功后事件
     * @param dataFlow
     * @param business 成功的事件业务数据封装对象
     */
    public static void invokeBusinessBSuccess(IOrderDataFlowContext dataFlow, Business business, JSONObject businessResponseData){
        multicastEvent(new InvokeBusinessBSuccessEvent("",dataFlow,business,businessResponseData));
    }

    /**
     * 调用业务系统成功后事件
     * @param dataFlow
     * @param business 成功的事件业务数据封装对象
     */
    public static void invokeBusinessBSuccess(IOrderDataFlowContext dataFlow, Business business){
        multicastEvent(new InvokeBusinessBSuccessEvent("",dataFlow,business));
    }

    /**
     * 调用业务系统成功后事件
     * @param dataFlow
     * @param business 成功的事件业务数据封装对象
     */
    public static void invokeBusinessISuccess(IOrderDataFlowContext dataFlow, Business business){
        multicastEvent(new InvokeBusinessISuccessEvent("",dataFlow,business));
    }


    /**
     * 数据返回事件
     * @param dataFlow 数据流
     */
    public static void dataResponse(IOrderDataFlowContext dataFlow,String responseData,Map<String,String> headers){
        multicastEvent(new DataResponseEvent("",dataFlow,responseData,headers));
    }



    /***********************************************发布侦听 结束***************************************************************/
}
