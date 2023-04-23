package com.java110.dto.inspection;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @ClassName FloorDto
 * @Description 巡检任务明细数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InspectionTaskDetailDto extends PageDto implements Serializable {

    public static final String SEND_FLAG_Y = "Y";//已经推送
    public static final String SEND_FLAG_N = "N";//未推送数据

    private String inspectionId;
    private String inspectionName;
    private String state;
    private String stateName;
    private String communityId;
    private String taskId;
    private String taskDetailId;
    private String patrolType;
    private String patrolTypeName;
    private String description;
    private String inspectionPlanName;
    private String routeName;
    private String actUserName;
    private String planUserName;
    private String planUserId;
    private String signType;
    private String signTypeName;
    private String inspectionPlanId;
    private String inspectionRouteId;
    private String taskState;
    private String taskStateName;

    private String planInsTime;
    private String planEndTime;
    private String createTime;
    private String pointStartTime;
    private String pointEndTime;
    private String sortNumber;
    private String pointObjId;

    //经度
    private String longitude;

    //维度
    private String latitude;

    //巡检点打卡时间
    private String inspectionTime;
    private String inspectionStartTime;
    private String inspectionEndTime;

    //签到状态
    private String inspectionState;
    private String inspectionStateName;

    private String inspectionTimeFlag;

    //当前日期
    private String nowTime;
    //二维码巡检时 的时间
    private String qrCodeTime;
    private String queryTime;

    private String statusCd = "0";
    private String itemId;

    private String sendFlag;

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
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

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getInspectionTimeFlag() {
        return inspectionTimeFlag;
    }

    public void setInspectionTimeFlag(String inspectionTimeFlag) {
        this.inspectionTimeFlag = inspectionTimeFlag;
    }

    public String getInspectionStartTime() {
        return inspectionStartTime;
    }

    public void setInspectionStartTime(String inspectionStartTime) {
        this.inspectionStartTime = inspectionStartTime;
    }

    public String getInspectionEndTime() {
        return inspectionEndTime;
    }

    public void setInspectionEndTime(String inspectionEndTime) {
        this.inspectionEndTime = inspectionEndTime;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPlanUserId() {
        return planUserId;
    }

    public void setPlanUserId(String planUserId) {
        this.planUserId = planUserId;
    }

    public String getQrCodeTime() {
        return qrCodeTime;
    }

    public void setQrCodeTime(String qrCodeTime) {
        this.qrCodeTime = qrCodeTime;
    }

    public String getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag;
    }

    public String getPointObjId() {
        return pointObjId;
    }

    public void setPointObjId(String pointObjId) {
        this.pointObjId = pointObjId;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }
}
