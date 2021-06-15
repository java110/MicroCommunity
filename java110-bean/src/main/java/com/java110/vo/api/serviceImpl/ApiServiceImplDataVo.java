package com.java110.vo.api.serviceImpl;

import java.io.Serializable;
import java.util.Date;

public class ApiServiceImplDataVo implements Serializable {

    private String serviceBusinessId;
private String businessTypeCd;
private String name;
private String invokeType;
private String url;
private String messageTopic;
private String timeout;
private String retryCount;
private String description;
public String getServiceBusinessId() {
        return serviceBusinessId;
    }
public void setServiceBusinessId(String serviceBusinessId) {
        this.serviceBusinessId = serviceBusinessId;
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
public String getDescription() {
        return description;
    }
public void setDescription(String description) {
        this.description = description;
    }



}
