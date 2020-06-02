package com.java110.log.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.order.OrderList;

/**
 * 订单服务业务处理接口
 *
 * 订单受理
 * Created by wuxw on 2017/4/11.
 */
public interface ILogServiceSMO {

    /**
     * 保存日志信息
     * {
     *     "transactionId":"交易流水号",
     *     "dataFlowId":"上下文对象",
     *     "ip":"ip",
     *     "port":"端口",
     *     "srcIp":"调用方IP",
     *     "srcPort":"调用方端口",
     *     "appId":"应用ID",
     *     "userId":"用户ID",
     *     "serviceCode":"服务编码",
     *     "serviceName":"服务名称",
     *     "timestamp":"时间储",
     *     "logStatus":"记录状态",
     *     "requestMessage":"请求信息",
     *     "responseMessage":"返回信息"
     * }
     * @param logMessage 需要保存的日志信息
     *                   从kafka 中读取日志消息保存
     */
    public void saveLogMessage(String logMessage);

}
