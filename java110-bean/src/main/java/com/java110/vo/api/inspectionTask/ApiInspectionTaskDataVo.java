package com.java110.vo.api.inspectionTask;

import java.io.Serializable;
import java.util.Date;

public class ApiInspectionTaskDataVo implements Serializable {

    private String planUserId;
    private String actInsTime;
    private String planInsTime;
    private String statusCd;
    private String actUserName;
    private String operate;
    private String signType;
    private String signTypeName;
    private String inspectionPlanId;
    private String planUserName;
    private String communityId;
    private String bId;
    private String actUserId;
    private String taskId;
    private String state;
    private String stateName;

    public String getPlanUserId() {
        return planUserId;
    }

    public void setPlanUserId(String planUserId) {
        this.planUserId = planUserId;
    }

    public String getActInsTime() {
        return actInsTime;
    }

    public void setActInsTime(String actInsTime) {
        this.actInsTime = actInsTime;
    }

    public String getPlanInsTime() {
        return planInsTime;
    }

    public void setPlanInsTime(String planInsTime) {
        this.planInsTime = planInsTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getInspectionPlanId() {
        return inspectionPlanId;
    }

    public void setInspectionPlanId(String inspectionPlanId) {
        this.inspectionPlanId = inspectionPlanId;
    }

    public String getPlanUserName() {
        return planUserName;
    }

    public void setPlanUserName(String planUserName) {
        this.planUserName = planUserName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getBId() {
        return bId;
    }

    public void setBId(String bId) {
        this.bId = bId;
    }

    public String getActUserId() {
        return actUserId;
    }

    public void setActUserId(String actUserId) {
        this.actUserId = actUserId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSignTypeName() {
        return signTypeName;
    }

    public void setSignTypeName(String signTypeName) {
        this.signTypeName = signTypeName;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
