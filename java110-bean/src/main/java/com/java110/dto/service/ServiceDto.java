package com.java110.dto.service;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 服务数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ServiceDto extends PageDto implements Serializable {

    private String businessTypeCd;
private String method;
private String provideAppId;
private String serviceCode;
private String retryCount;
private String messageQueueName;
private String url;
private String timeout;
private String isInstance;
private String name;
private String serviceName;
private String serviceUrl;
private String serviceId;
private String seq;
private String appId;


    private Date createTime;

    private String statusCd = "0";


    public String getBusinessTypeCd() {
        return businessTypeCd;
    }
public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }
public String getMethod() {
        return method;
    }
public void setMethod(String method) {
        this.method = method;
    }
public String getProvideAppId() {
        return provideAppId;
    }
public void setProvideAppId(String provideAppId) {
        this.provideAppId = provideAppId;
    }
public String getServiceCode() {
        return serviceCode;
    }
public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
public String getRetryCount() {
        return retryCount;
    }
public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }
public String getMessageQueueName() {
        return messageQueueName;
    }
public void setMessageQueueName(String messageQueueName) {
        this.messageQueueName = messageQueueName;
    }
public String getUrl() {
        return url;
    }
public void setUrl(String url) {
        this.url = url;
    }
public String getTimeout() {
        return timeout;
    }
public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
public String getIsInstance() {
        return isInstance;
    }
public void setIsInstance(String isInstance) {
        this.isInstance = isInstance;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getServiceId() {
        return serviceId;
    }
public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
public String getSeq() {
        return seq;
    }
public void setSeq(String seq) {
        this.seq = seq;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
