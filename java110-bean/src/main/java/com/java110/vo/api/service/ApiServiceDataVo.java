package com.java110.vo.api.service;

import java.io.Serializable;
import java.util.Date;

public class ApiServiceDataVo implements Serializable {
    private String serviceId;
private String name;
private String serviceCode;
private String businessTypeCd;
private String seq;
private String messageQueueName;
private String isInstance;
private String url;
private String method;
private String timeout;
private String retryCount;
private String provideAppId;
public String getServiceId() {
        return serviceId;
    }
public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
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
public String getSeq() {
        return seq;
    }
public void setSeq(String seq) {
        this.seq = seq;
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
public String getTimeout() {
        return timeout;
    }
public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
public String getRetryCount() {
        return retryCount;
    }
public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }
public String getProvideAppId() {
        return provideAppId;
    }
public void setProvideAppId(String provideAppId) {
        this.provideAppId = provideAppId;
    }



}
