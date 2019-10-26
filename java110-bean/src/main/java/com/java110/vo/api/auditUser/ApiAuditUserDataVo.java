package com.java110.vo.api.auditUser;

import java.io.Serializable;
import java.util.Date;

public class ApiAuditUserDataVo implements Serializable {

    private String auditUserId;
private String userId;
private String userName;
private String auditLink;
private String objCode;
public String getAuditUserId() {
        return auditUserId;
    }
public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
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



}
