package com.java110.api.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.log.LoggerFactory;
import org.slf4j.Logger;

import java.text.ParseException;

/**
 * 主要目的将soService 方法拆分为校验部分 和业务处理部分
 * Created by wuxw on 2018/11/15.
 */
public abstract class AbstractServiceApiListener extends AbstractServiceApiDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractServiceApiListener.class);


    /**
     * 业务处理
     * @param event
     */
    public final void soService(ServiceDataFlowEvent event) throws ParseException {

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();

        logger.debug("API服务 --- 请求参数为：{}", reqJson.toJSONString());

        validate(event, reqJson);


        doSoService(event, dataFlowContext, reqJson);

        logger.debug("API服务 --- 返回报文信息：{}", dataFlowContext.getResponseEntity());

    }

    /**
     * 数据格式校验方法
     * @param event 事件对象
     * @param reqJson 请求报文数据
     */
    protected abstract void validate(ServiceDataFlowEvent event, JSONObject reqJson);


    /**
     * 业务处理类
     * @param event  事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     */
    protected abstract void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException;


    @Override
    public int getOrder() {
        return 0;
    }

}
