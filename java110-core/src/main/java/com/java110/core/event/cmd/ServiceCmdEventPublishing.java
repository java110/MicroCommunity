package com.java110.core.event.cmd;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.center.DataFlowListenerOrderComparator;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.CmdListenerDto;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ExceptionUtil;
import org.slf4j.Logger;

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
public class ServiceCmdEventPublishing {
    private static Logger logger = LoggerFactory.getLogger(ServiceCmdEventPublishing.class);

    private static Executor taskExecutor;

    //默认 线程数 100
    private static final int DEFAULT_THREAD_NUM = 100;

    /**
     * 保存侦听实例信息，一般启动时加载
     */
    private static final List<CmdListenerDto> listeners = new ArrayList<CmdListenerDto>();

    /**
     * 根据 事件类型查询侦听
     */
    private static final Map<String, List<ServiceCmdListener>> cacheListenersMap = new HashMap<String, List<ServiceCmdListener>>();

    /**
     * 添加 侦听，这个只有启动时，单线程 处理，所以是线程安全的
     *
     * @param listener
     */
    public static void addListener(CmdListenerDto listener) {
        listeners.add(listener);
    }

    /**
     * 获取侦听（全部侦听）
     *
     * @return
     */
    public static List<CmdListenerDto> getListeners() {
        return listeners;
    }

    /**
     * 根据是否实现了某个接口，返回侦听
     *
     * @param serviceCode
     * @return
     * @since 1.8
     */
    public static List<ServiceCmdListener> getListeners(String serviceCode) {

        Assert.hasLength(serviceCode, "获取需要发布的事件处理侦听时，传递事件为空，请检查");

        String needCachedServiceCode = serviceCode;
        //先从缓存中获取，为了提升效率
        if (cacheListenersMap.containsKey(needCachedServiceCode)) {
            return cacheListenersMap.get(needCachedServiceCode);
        }

        List<ServiceCmdListener> cmdListeners = new ArrayList<ServiceCmdListener>();
        for (CmdListenerDto listenerBean : getListeners()) {
            //ServiceCmdListener listener = ApplicationContextFactory.getBean(listenerBean.getBeanName(), ServiceCmdListener.class);
            ServiceCmdListener listener = ApplicationContextFactory.getBean(listenerBean.getBeanName(), ServiceCmdListener.class);
            if (listenerBean.getServiceCode().equals(serviceCode)) {
                cmdListeners.add(listener);
            }
        }

        //这里排序
        DataFlowListenerOrderComparator.sort(cmdListeners);


        //将数据放入缓存中
        if (cmdListeners.size() > 0) {
            cacheListenersMap.put(needCachedServiceCode, cmdListeners);
        }
        return cmdListeners;
    }


    /**
     * 发布事件
     *
     * @param cmdDataFlowContext
     */
    public static void multicastEvent(ICmdDataFlowContext cmdDataFlowContext) throws Exception {
        Assert.notNull(cmdDataFlowContext.getServiceCode(), "当前没有可处理的业务信息！");
        //todo 根据cmd serviceCode 发布事件
        multicastEvent(cmdDataFlowContext.getServiceCode(), cmdDataFlowContext, null);
    }


    /**
     * 发布事件
     *
     * @param serviceCode
     * @param dataFlowContext
     */
    public static void multicastEvent(String serviceCode, ICmdDataFlowContext dataFlowContext) throws Exception {
        multicastEvent(serviceCode, dataFlowContext, null);
    }

    /**
     * 发布事件
     *
     * @param serviceCode
     * @param dataFlowContext 这个订单信息，以便于 侦听那边需要用
     */
    public static void multicastEvent(String serviceCode, ICmdDataFlowContext dataFlowContext, String asyn) throws Exception {
        try {
            //todo 组装事件
            CmdEvent targetDataFlowEvent = new CmdEvent(serviceCode, dataFlowContext);

            //todo 发布事件
            multicastEvent(serviceCode, targetDataFlowEvent, asyn);
        } catch (Exception e) {
            logger.error("发布侦听失败，失败原因为：", e);
            //throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, e.getMessage());
            throw e;
        }

    }


    /**
     * 发布事件
     *
     * @param event
     * @param asyn  A 表示异步处理
     */
    public static void multicastEvent(String serviceCode, final CmdEvent event, String asyn) throws Exception {
        //todo 根据serviceCode 去寻找 处理的Cmd处理类 如果java类中 @Java110Cmd(serviceCode = "xx.xx") 写了该注解就会被寻找到
        List<ServiceCmdListener> listeners = getListeners(serviceCode);
        //这里判断 serviceCode + httpMethod 的侦听，如果没有注册直接报错。
        if (listeners == null || listeners.size() == 0) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR,
                    "服务【" + serviceCode + "】当前不支持");
        }
        for (final ServiceCmdListener listener : listeners) {

            if (CommonConstant.PROCESS_ORDER_ASYNCHRONOUS.equals(asyn)) { //todo 异步处理,一般很少用

                Executor executor = getTaskExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            invokeListener(listener, event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            } else {
                // todo 通过同步的方式调用CMDjava类
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
     * 执行 根据serviceCode 找到的cmd 类
     *
     * @param listener the ApplicationListener to invoke
     * @param event    the current event to propagate
     * @since 4.1
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static void invokeListener(ServiceCmdListener listener, CmdEvent event) throws Exception {
        try {
            //todo 获取 cmd 上下文对象
            ICmdDataFlowContext dataFlowContext = event.getCmdDataFlowContext();
            //todo 获取请求数据
            JSONObject reqJson = dataFlowContext.getReqJson();

            logger.debug("API服务 --- 请求参数为：{}", reqJson.toJSONString());

            //todo 调用 cmd的校验方法
            listener.validate(event, dataFlowContext, reqJson);

            //todo 调用 cmd的业务处理方法
            listener.doCmd(event, dataFlowContext, reqJson);

            //logger.debug("API服务 --- 返回报文信息：{}", dataFlowContext.getResponseEntity());
            //   listener.cmd(event);
        } catch (Throwable e) {
            LoggerEngine.error("发布侦听失败" + e);
            throw e;
        }
    }


}
