package com.java110.dto.resourceStore;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * 资源订单信息
 */
public class ResourceOrderDto extends PageDto implements Serializable {

    //工作流实例ID
    private String processInstanceId;

    private String taskId;

    //资源订单ID
    private String resOrderId;

    //商户ID
    private String storeId;

    // 资源类型 入库还是出库
    private String resOrderType;

    // 资源状态
    private String state;

    //
    private String auditCode;

    private String auditMessage;


    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getResOrderId() {
        return resOrderId;
    }

    public void setResOrderId(String resOrderId) {
        this.resOrderId = resOrderId;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
}
