package com.java110.po.audit;

import java.io.Serializable;

/**
 * @ClassName AuditUserPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/27 13:55
 * @Version 1.0
 * add by wuxw 2020/5/27
 **/
public class AuditUserPo implements Serializable {

    private String auditUserId;
    private String storeId;
    private String userId;
    private String userName;
    private String auditLink;
    private String objCode;
    private String statusCd;

    public String getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getAuditLink() {
        return auditLink;
    }

    public void setAuditLink(String auditLink) {
        this.auditLink = auditLink;
    }

    public String getObjCode() {
        return objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
