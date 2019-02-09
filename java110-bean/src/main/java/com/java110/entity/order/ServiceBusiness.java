package com.java110.entity.order;

import java.io.Serializable;

/**
 * @author wux
 * @create 2019-02-05 下午10:22
 * @desc 服务业务类
 **/
public class ServiceBusiness implements Serializable {

    private long serviceBusinessId;

    /**
     * 业务类型
     */
    private  String businessTypeCd;

    /**
     * 调用类型 1 http-post(微服务内应用) 2 webservice 3 kafka 4 http-post(微服务之外应用)
     */
    private String invokeType;

    /**
     * 同步时的url地址 当invokeType 为 3 时写kafka topic
     */
    private String url;

    private String messageTopic;


    private int timeout;

    private int retryCount;


    public long getServiceBusinessId() {
        return serviceBusinessId;
    }

    public void setServiceBusinessId(long serviceBusinessId) {
        this.serviceBusinessId = serviceBusinessId;
    }

    public String getBusinessTypeCd() {
        return businessTypeCd;
    }

    public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }

    public String getInvokeType() {
        return invokeType;
    }

    public void setInvokeType(String invokeType) {
        this.invokeType = invokeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessageTopic() {
        return messageTopic;
    }

    public void setMessageTopic(String messageTopic) {
        this.messageTopic = messageTopic;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
