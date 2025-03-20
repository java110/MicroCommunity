package com.java110.dto.inspection;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 活动数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InspectionTaskDto extends PageDto implements Serializable {

    public static final String STATE_NO_START = "20200405";
    public static final String STATE_DOING = "20200406"; // 20200406	巡检中
    public static final String STATE_FINISH = "20200407";//20200407	巡检完成

    private String planUserId;
    private String actInsTime;
    private String planInsTime;
    private String planEndTime;
    private String todayPlanInsTime; // 今天计划时间
    private String actUserName;
    private String signType;
    private String signTypeName;
    private String inspectionPlanId;
    private String inspectionPlanName;
    private String planUserName;
    private String communityId;
    private String[] communityIds;
    private String actUserId;
    private String taskId;
    private String state;
    private String stateName;
    private Date scopeTime;
    private String ipStaffId;
    private String[] states;

    private String startTime;
    private String endTime;
    private String originalPlanUserId;
    private String originalPlanUserName;
    private String transferDesc;
    private String taskType;

    private Date createTime;

    private String statusCd = "0";

    //查询当天巡检任务标识
    private String dayTask;

    private String orderByDesc;

    private String inspectionId;

    private String inspectionRouteId;

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

    public String getSignTypeName() {
        return signTypeName;
    }

    public void setSignTypeName(String signTypeName) {
        this.signTypeName = signTypeName;
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

    public String getInspectionPlanName() {
        return inspectionPlanName;
    }

    public void setInspectionPlanName(String inspectionPlanName) {
        this.inspectionPlanName = inspectionPlanName;
    }

    public Date getScopeTime() {
        return scopeTime;
    }

    public void setScopeTime(Date scopeTime) {
        this.scopeTime = scopeTime;
    }

    public String getIpStaffId() {
        return ipStaffId;
    }

    public void setIpStaffId(String ipStaffId) {
        this.ipStaffId = ipStaffId;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public String getTodayPlanInsTime() {
        return todayPlanInsTime;
    }

    public void setTodayPlanInsTime(String todayPlanInsTime) {
        this.todayPlanInsTime = todayPlanInsTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDayTask() {
        return dayTask;
    }

    public void setDayTask(String dayTask) {
        this.dayTask = dayTask;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }


    public String getOriginalPlanUserId() {
        return originalPlanUserId;
    }

    public void setOriginalPlanUserId(String originalPlanUserId) {
        this.originalPlanUserId = originalPlanUserId;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getOrderByDesc() {
        return orderByDesc;
    }

    public void setOrderByDesc(String orderByDesc) {
        this.orderByDesc = orderByDesc;
    }

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getInspectionRouteId() {
        return inspectionRouteId;
    }

    public void setInspectionRouteId(String inspectionRouteId) {
        this.inspectionRouteId = inspectionRouteId;
    }

    public String[] getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(String[] communityIds) {
        this.communityIds = communityIds;
    }
}
