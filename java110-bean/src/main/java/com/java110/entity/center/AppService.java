package com.java110.entity.center;

import java.io.Serializable;
import java.util.Map;

/**
 * 提供服务
 * Created by wuxw on 2018/4/14.
 */
public class AppService implements Serializable{

    private String serviceId;

    private String serviceCode;

    private String businessTypeCd;

    private String name;

    private int seq;

    //消息队里名称 只有异步时有用
    private String messageQueueName;

    private String url;

    //只有webservice时才有用
    private String method;

    private String isInstance;



    private int timeOut;

    private int retryCount;

    private String statusCd;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }


    public String getBusinessTypeCd() {
        return businessTypeCd;
    }

    public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getMessageQueueName() {
        return messageQueueName;
    }

    public void setMessageQueueName(String messageQueueName) {
        this.messageQueueName = messageQueueName;
    }

    public String getIsInstance() {
        return isInstance;
    }

    public void setIsInstance(String isInstance) {
        this.isInstance = isInstance;
    }

    public AppService builder(Map serviceInfo){
        this.setBusinessTypeCd(serviceInfo.get("business_type_cd").toString());
        this.setMessageQueueName(serviceInfo.get("messageQueueName") == null ? null :serviceInfo.get("messageQueueName").toString());
        this.setMethod(serviceInfo.get("method")==null ? null:serviceInfo.get("method").toString());
        this.setName(serviceInfo.get("name").toString());
        this.setRetryCount(Integer.parseInt(serviceInfo.get("retry_count").toString()));
        this.setSeq(Integer.parseInt(serviceInfo.get("seq").toString()));
        this.setServiceCode(serviceInfo.get("service_code").toString());
        this.setTimeOut(Integer.parseInt(serviceInfo.get("timeout").toString()));
        this.setUrl(serviceInfo.get("url") == null ? null : serviceInfo.get("url").toString());
        this.setServiceId(serviceInfo.get("service_id").toString());
        this.setIsInstance(serviceInfo.get("is_instance").toString());
        this.setStatusCd("0");
        return this;
    }

    public static AppService newInstance(){
        return new AppService();
    }


}
