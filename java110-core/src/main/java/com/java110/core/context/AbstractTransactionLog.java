package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 交互日志抽象类
 * Created by wuxw on 2018/6/9.
 */
public abstract class AbstractTransactionLog  implements TransactionLog {

    private final static Logger logger = LoggerFactory.getLogger(AbstractTransactionLog.class);

    private String port;

    private String logStatus;

    private String requestMessage;

    private String responseMessage;

    public String getHostIp() {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            logger.error("获取主机Ip失败",e);
            ip = "-1";
        }
        return ip;
    }

    /**
     * 时间戳
     * @return
     */
    public String getTimestamp(){
        return DateUtil.getCurrentDate().getTime()+"";
    }


    @Override
    public String getLogStatus() {
        return logStatus;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    @Override
    public String getPort() {
        return port;
    }

    /**
     * 预构建
     * @param reqInfo
     * @param headerAll
     */
    protected void preBuilder(String reqInfo, Map<String,String> headerAll){

        if(headerAll != null && headerAll.containsKey("port")){
            this.port = headerAll.get("port");
        }

    }

    /**
     * 重新构建 TransactionLog 对象 主要用于服务调用方
     * @return
     */
    public TransactionLog reBuilder(String requestMessage,String responseMessage,String logStatus){

        this.logStatus = logStatus;
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;

        return this;
    }

    /**
     * 重新构建 TransactionLog 对象 主要用于服务提供方
     * @return
     */
    public TransactionLog reBuilder(String appId,String userId,String requestMessage,String responseMessage,String logStatus){

        this.logStatus = logStatus;
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;
        this.setAppId(appId);
        this.setUserId(userId);
        return this;
    }


    @Override
    public String toString() {
        //return JSONObject.toJSONString(this);
        JSONObject logMessage = JSONObject.parseObject("{}");
        logMessage.put("transactionId",getTransactionId());
        logMessage.put("dataFlowId",getDataFlowId());
        logMessage.put("ip",getHostIp());
        logMessage.put("port", getPort());
        logMessage.put("appId",getAppId());
        logMessage.put("userId",getUserId());
        logMessage.put("serviceCode",getServiceCode());
        logMessage.put("serviceName",getServiceName());
        logMessage.put("timestamp",getTimestamp());
        logMessage.put("logStatus",getLogStatus());
        logMessage.put("requestMessage",getRequestMessage());
        logMessage.put("responseMessage",getResponseMessage());
        return logMessage.toJSONString(logMessage,SerializerFeature.WriteNullStringAsEmpty);
    }
}
