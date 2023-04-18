package com.java110.dto.maintainance;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 保养任务数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MaintainanceTaskDto extends PageDto implements Serializable {

    public static final String STATE_WAIT = "20200405";
    public static final String STATE_AUDIT = "20200406";
    public static final String STATE_FINISH = "20200407";
    public static final String STATE_TIMEOUT = "20200408";
    public static final String STATE_NO = "20200409";

    private String planUserId;
    private String actInsTime;
    private String planInsTime;
    private String originalPlanUserName;
    private String transferDesc;
    private String actUserName;
    private String authDesc;
    private String originalPlanUserId;
    private String taskType;
    private String planId;
    private String planName;
    private String planEndTime;
    private String planUserName;
    private String communityId;
    private String actUserId;
    private String taskId;
    private String mpsId;
    private String state;
    private String[] states;
    private String stateName;


    private Date createTime;

    private String statusCd = "0";


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

    public String getOriginalPlanUserName() {
        return originalPlanUserName;
    }

    public void setOriginalPlanUserName(String originalPlanUserName) {
        this.originalPlanUserName = originalPlanUserName;
    }

    public String getTransferDesc() {
        return transferDesc;
    }

    public void setTransferDesc(String transferDesc) {
        this.transferDesc = transferDesc;
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    public String getAuthDesc() {
        return authDesc;
    }

    public void setAuthDesc(String authDesc) {
        this.authDesc = authDesc;
    }

    public String getOriginalPlanUserId() {
        return originalPlanUserId;
    }

    public void setOriginalPlanUserId(String originalPlanUserId) {
        this.originalPlanUserId = originalPlanUserId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
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

    public String getMpsId() {
        return mpsId;
    }

    public void setMpsId(String mpsId) {
        this.mpsId = mpsId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }
}
