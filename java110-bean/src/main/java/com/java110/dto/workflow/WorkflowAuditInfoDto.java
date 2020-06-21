package com.java110.dto.workflow;

import java.io.Serializable;

/**
 * @ClassName WorkflowAuditInfoDto
 * @Description TODO
 * @Author wuxw
 * @Date 2020/6/21 22:10
 * @Version 1.0
 * add by wuxw 2020/6/21
 **/
public class WorkflowAuditInfoDto implements Serializable {

    private String businessKey;

    private String auditName;

    private String auditTime;

    private String duration;

    private String userId;

    private String message;


    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
