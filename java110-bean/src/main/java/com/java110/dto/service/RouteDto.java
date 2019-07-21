package com.java110.dto.service;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 路由数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RouteDto extends PageDto implements Serializable {

    private String invokeLimitTimes;
private String orderTypeCd;
private String appId;
private String id;
private String serviceId;
private String invokeModel;


    private Date createTime;

    private String statusCd = "0";


    public String getInvokeLimitTimes() {
        return invokeLimitTimes;
    }
public void setInvokeLimitTimes(String invokeLimitTimes) {
        this.invokeLimitTimes = invokeLimitTimes;
    }
public String getOrderTypeCd() {
        return orderTypeCd;
    }
public void setOrderTypeCd(String orderTypeCd) {
        this.orderTypeCd = orderTypeCd;
    }
public String getAppId() {
        return appId;
    }
public void setAppId(String appId) {
        this.appId = appId;
    }
public String getId() {
        return id;
    }
public void setId(String id) {
        this.id = id;
    }
public String getServiceId() {
        return serviceId;
    }
public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
public String getInvokeModel() {
        return invokeModel;
    }
public void setInvokeModel(String invokeModel) {
        this.invokeModel = invokeModel;
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
}
