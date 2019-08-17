package com.java110.dto.service;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 服务实现数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ServiceBusinessDto extends PageDto implements Serializable {

    private String businessTypeCd;
private String invokeType;
private String messageTopic;
private String retryCount;
private String serviceBusinessId;
private String url;
private String timeout;
private String name;
private String description;


    private Date createTime;

    private String statusCd = "0";


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
public String getMessageTopic() {
        return messageTopic;
    }
public void setMessageTopic(String messageTopic) {
        this.messageTopic = messageTopic;
    }
public String getRetryCount() {
        return retryCount;
    }
public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }
public String getServiceBusinessId() {
        return serviceBusinessId;
    }
public void setServiceBusinessId(String serviceBusinessId) {
        this.serviceBusinessId = serviceBusinessId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
