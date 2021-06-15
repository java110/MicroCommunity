package com.java110.dto.corder;

import com.java110.dto.PageDto;
import com.java110.vo.api.corder.CbusinessVo;

import java.io.Serializable;
import java.util.List;

public class CorderDto extends PageDto implements Serializable {
    private String oId;
    private String appId;
    private String appName;
    private String name;
    private String extTransactionId;
    private String userId;
    private String requestTime;
    private String createTime;
    private String orderTypeCd;
    private String finishTime;
    private String remark;
    private String statusCd;
    private List<CbusinessVo> cBusiness;
    private String orderTypeCdName;
    private String userName;

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getExtTransactionId() {
        return extTransactionId;
    }

    public void setExtTransactionId(String extTransactionId) {
        this.extTransactionId = extTransactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderTypeCd() {
        return orderTypeCd;
    }

    public void setOrderTypeCd(String orderTypeCd) {
        this.orderTypeCd = orderTypeCd;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public List<CbusinessVo> getcBusiness() {
        return cBusiness;
    }

    public void setcBusiness(List<CbusinessVo> cBusiness) {
        this.cBusiness = cBusiness;
    }

    public String getOrderTypeCdName() {
        return orderTypeCdName;
    }

    public void setOrderTypeCdName(String orderTypeCdName) {
        this.orderTypeCdName = orderTypeCdName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
