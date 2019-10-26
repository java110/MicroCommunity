package com.java110.entity.audit;

import java.io.Serializable;

/**
 * 审核用户
 */
public class AuditUser implements Serializable {

    private String auditUserId;

    // 审核用户ID
    private String userId;

    private String userName;

    // 审核环节 如部门经理审核 ， 财务审核 ，采购人员采购
    private String auditLink;

    //流程对象编码
    private String objCode;


    public String getObjCode() {
        return objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuditLink() {
        return auditLink;
    }

    public void setAuditLink(String auditLink) {
        this.auditLink = auditLink;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }
}
