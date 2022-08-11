package com.java110.vo.api.inspectionTaskDetail;

import com.java110.vo.api.junkRequirement.PhotoVo;

import java.io.Serializable;
import java.util.List;

public class ApiInspectionTaskDetailDataVo implements Serializable {

    private String inspectionId;
    private String operate;
    private String statusCd;
    private String inspectionName;
    private String state;
    private String stateName;
    private String communityId;
    private String bId;
    private String taskId;
    private String taskDetailId;

    private List<PhotoVo> photos;

    private String patrolType;
    private String patrolTypeName;
    private String inspectionPlanName;
    private String routeName;
    private String actUserName;
    private String planUserName;
    private String signType;
    private String signTypeName;
    private String inspectionPlanId;
    private String inspectionRouteId;
    private String taskState;
    private String taskStateName;

    private String pointStartTime;
    private String pointEndTime;
    private String sortNumber;

    private String description;

    private String planInsTime;
    private String planEndTime;

    private String createTime;

    //经度
    private String longitude;

    //维度
    private String latitude;

    //巡检点打卡时间
    private String inspectionTime;

    //签到状态
    private String inspectionState;
    private String inspectionStateName;
    private String itemId;

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(String taskDetailId) {
        this.taskDetailId = taskDetailId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPatrolType() {
        return patrolType;
    }

    public void setPatrolType(String patrolType) {
        this.patrolType = patrolType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PhotoVo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoVo> photos) {
        this.photos = photos;
    }

    public String getPatrolTypeName() {
        return patrolTypeName;
    }

    public void setPatrolTypeName(String patrolTypeName) {
        this.patrolTypeName = patrolTypeName;
    }

    public String getPlanInsTime() {
        return planInsTime;
    }

    public void setPlanInsTime(String planInsTime) {
        this.planInsTime = planInsTime;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(String inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    public String getInspectionState() {
        return inspectionState;
    }

    public void setInspectionState(String inspectionState) {
        this.inspectionState = inspectionState;
    }

    public String getInspectionStateName() {
        return inspectionStateName;
    }

    public void setInspectionStateName(String inspectionStateName) {
        this.inspectionStateName = inspectionStateName;
    }

    public String getInspectionPlanName() {
        return inspectionPlanName;
    }

    public void setInspectionPlanName(String inspectionPlanName) {
        this.inspectionPlanName = inspectionPlanName;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    public String getPlanUserName() {
        return planUserName;
    }

    public void setPlanUserName(String planUserName) {
        this.planUserName = planUserName;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSignTypeName() {
        return signTypeName;
    }

    public void setSignTypeName(String signTypeName) {
        this.signTypeName = signTypeName;
    }

    public String getInspectionPlanId() {
        return inspectionPlanId;
    }

    public void setInspectionPlanId(String inspectionPlanId) {
        this.inspectionPlanId = inspectionPlanId;
    }

    public String getInspectionRouteId() {
        return inspectionRouteId;
    }

    public void setInspectionRouteId(String inspectionRouteId) {
        this.inspectionRouteId = inspectionRouteId;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskStateName() {
        return taskStateName;
    }

    public void setTaskStateName(String taskStateName) {
        this.taskStateName = taskStateName;
    }

    public String getPointStartTime() {
        return pointStartTime;
    }

    public void setPointStartTime(String pointStartTime) {
        this.pointStartTime = pointStartTime;
    }

    public String getPointEndTime() {
        return pointEndTime;
    }

    public void setPointEndTime(String pointEndTime) {
        this.pointEndTime = pointEndTime;
    }

    public String getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(String sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
