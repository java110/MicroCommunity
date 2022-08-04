package com.java110.vo.api.serviceRegister;

import java.io.Serializable;
import java.util.Date;

public class ApiServiceRegisterDataVo implements Serializable {

    private String id;
    private String appId;
    private String serviceId;
    private String orderTypeCd;
    private String invokeLimitTimes;
    private String invokeModel;

    private String appName;
    private String serviceName;
    private String serviceCode;
    private String serviceUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getOrderTypeCd() {
        return orderTypeCd;
    }

    public void setOrderTypeCd(String orderTypeCd) {
        this.orderTypeCd = orderTypeCd;
    }

    public String getInvokeLimitTimes() {
        return invokeLimitTimes;
    }

    public void setInvokeLimitTimes(String invokeLimitTimes) {
        this.invokeLimitTimes = invokeLimitTimes;
    }

    public String getInvokeModel() {
        return invokeModel;
    }

    public void setInvokeModel(String invokeModel) {
        this.invokeModel = invokeModel;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
