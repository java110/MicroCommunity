package com.java110.dto.auditMessage;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 审核原因数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AuditMessageDto extends PageDto implements Serializable {

    private String auditOrderType;
    private String auditMessageId;
    private String auditOrderId;
    private String state;
    private String storeId;
    private String userName;
    private String message;
    private String userId;


    private Date createTime;

    private String statusCd = "0";


    public String getAuditOrderType() {
        return auditOrderType;
    }

    public void setAuditOrderType(String auditOrderType) {
        this.auditOrderType = auditOrderType;
    }

    public String getAuditMessageId() {
        return auditMessageId;
    }

    public void setAuditMessageId(String auditMessageId) {
        this.auditMessageId = auditMessageId;
    }

    public String getAuditOrderId() {
        return auditOrderId;
    }

    public void setAuditOrderId(String auditOrderId) {
        this.auditOrderId = auditOrderId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
