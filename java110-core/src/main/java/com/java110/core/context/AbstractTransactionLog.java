package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.net.InetAddress;
import java.util.Map;

/**
 * 交互日志抽象类
 * Created by wuxw on 2018/6/9.
 */
public abstract class AbstractTransactionLog  implements TransactionLog {

    private final static Logger logger = LoggerFactory.getLogger(AbstractTransactionLog.class);

    protected String port;

    private String logStatus;

    private String requestMessage;

    private long costTime;

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
        return DateUtil.getNowDefault();
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

    public long getCostTime() {
        return costTime;
    }

    @Override
    public String getPort() {
        return port;
    }

    /**
     * 预构建
     * 如果不是http方式请求构建的对象情况下 port 为 -1 请在创建完对象后
     * 用这个
     * ServiceInfoListener serviceInfoListener =  ApplicationContextFactory.getBean("serviceInfoListener",ServiceInfoListener.class);
     * 对象刷一下 端口
     * @param reqInfo
     * @param headerAll
     */
    protected void preBuilder(String reqInfo, Map<String,String> headerAll){

        if(headerAll != null && headerAll.containsKey("port")){
            this.port = headerAll.get("port");
        }else{
            this.port = "-1";
        }
    }

    /**
     * 重新构建 TransactionLog 对象 主要用于服务调用方
     * @return
     */
    public TransactionLog reBuilder(String requestMessage,String responseMessage,String logStatus,long costTime){

        this.logStatus = logStatus;
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;
        this.costTime = costTime;
        return this;
    }

    /**
     * 重新构建 TransactionLog 对象 主要用于服务提供方
     * @return
     */
    public TransactionLog reBuilder(String appId,String userId,String requestMessage,String responseMessage,String logStatus,long costTime){

        this.logStatus = logStatus;
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;
        this.setAppId(appId);
        this.setUserId(userId);
        this.costTime = costTime;
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
        logMessage.put("costTime",costTime);
        logMessage.put("requestMessage",getRequestMessage());
        logMessage.put("responseMessage",getResponseMessage());
        return logMessage.toJSONString(logMessage,SerializerFeature.WriteNullStringAsEmpty);
    }
}
