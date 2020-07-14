package com.java110.vo.api.resourceOrder;

import java.io.Serializable;

public class ApiResourceOrderDataVo implements Serializable {

    //工作流实例ID
    private String processInstanceId;

    private String taskId;

    private String userId;
    private String userName;


    //订单ID
    private String applyOrderId;

    //商户ID
    private String storeId;

    // 资源类型 入库还是出库
    private String resOrderType;
    private String resOrderTypeName;

    // 资源状态
    private String state;
    private String stateName;

    //
    private String auditCode;

    private String auditMessage;
    private String createTime;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getApplyOrderId() {
        return applyOrderId;
    }

    public void setApplyOrderId(String applyOrderId) {
        this.applyOrderId = applyOrderId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getResOrderType() {
        return resOrderType;
    }

    public void setResOrderType(String resOrderType) {
        this.resOrderType = resOrderType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuditCode() {
        return auditCode;
    }

    public void setAuditCode(String auditCode) {
        this.auditCode = auditCode;
    }

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }

    public String getResOrderTypeName() {
        return resOrderTypeName;
    }

    public void setResOrderTypeName(String resOrderTypeName) {
        this.resOrderTypeName = resOrderTypeName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
