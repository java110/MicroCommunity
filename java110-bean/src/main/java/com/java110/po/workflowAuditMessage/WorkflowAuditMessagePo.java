package com.java110.po.workflowAuditMessage;

import java.io.Serializable;

public class WorkflowAuditMessagePo implements Serializable {

    private String auditId;
    private String preAuditId;
    private String businessKey;
    private String auditPersonId;
    private String auditPerson;
    private String shareId;
    private String statusCd = "0";
    private String state;
    private String businessType;
    private String message;
    private String shareType;
    private String auditLink;

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public String getPreAuditId() {
        return preAuditId;
    }

    public void setPreAuditId(String preAuditId) {
        this.preAuditId = preAuditId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getAuditPersonId() {
        return auditPersonId;
    }

    public void setAuditPersonId(String auditPersonId) {
        this.auditPersonId = auditPersonId;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getAuditLink() {
        return auditLink;
    }

    public void setAuditLink(String auditLink) {
        this.auditLink = auditLink;
    }


}
