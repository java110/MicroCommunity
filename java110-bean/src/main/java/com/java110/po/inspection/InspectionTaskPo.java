package com.java110.po.inspection;

import java.io.Serializable;

/**
 * @ClassName InspectionPlanPo
 * @Description TODO 巡检计划对象
 * @Author wuxw
 * @Date 2020/5/28 14:23
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class InspectionTaskPo implements Serializable {

    private String taskId;
    private String communityId;
    private String inspectionPlanId;
    private String planInsTime;
    private String actInsTime;
    private String planUserId;
    private String planUserName;
    private String actUserId;
    private String actUserName;
    private String signType;
    private String ipStaffId;

    private String state;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getInspectionPlanId() {
        return inspectionPlanId;
    }

    public void setInspectionPlanId(String inspectionPlanId) {
        this.inspectionPlanId = inspectionPlanId;
    }

    public String getPlanInsTime() {
        return planInsTime;
    }

    public void setPlanInsTime(String planInsTime) {
        this.planInsTime = planInsTime;
    }

    public String getActInsTime() {
        return actInsTime;
    }

    public void setActInsTime(String actInsTime) {
        this.actInsTime = actInsTime;
    }

    public String getPlanUserId() {
        return planUserId;
    }

    public void setPlanUserId(String planUserId) {
        this.planUserId = planUserId;
    }

    public String getPlanUserName() {
        return planUserName;
    }

    public void setPlanUserName(String planUserName) {
        this.planUserName = planUserName;
    }

    public String getActUserId() {
        return actUserId;
    }

    public void setActUserId(String actUserId) {
        this.actUserId = actUserId;
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIpStaffId() {
        return ipStaffId;
    }

    public void setIpStaffId(String ipStaffId) {
        this.ipStaffId = ipStaffId;
    }
}
