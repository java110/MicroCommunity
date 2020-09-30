package com.java110.dto.order;

import com.java110.dto.PageDto;

import java.io.Serializable;

public class OrderDto extends PageDto implements Serializable {



    public static final String O_ID = "o-id";

    //受理单
    public static final String ORDER_TYPE_DEAL = "D";


    private String appId;
    private String extTransactionId;
    private String userId;
    private String userName;
    private String requestTime;
    private String orderTypeCd;
    private String finishTime;
    private String remark;

    private String oId;

    private String bId;
    private String statusCd;

    private String businessTypeCd;

    private String[] businessTypeCds;



    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getBusinessTypeCd() {
        return businessTypeCd;
    }

    public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }

    public String[] getBusinessTypeCds() {
        return businessTypeCds;
    }

    public void setBusinessTypeCds(String[] businessTypeCds) {
        this.businessTypeCds = businessTypeCds;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
