package com.java110.dto.maintainance;

import com.java110.dto.PageDto;
import com.java110.vo.api.junkRequirement.PhotoVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 保养明细数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MaintainanceTaskDetailDto extends PageDto implements Serializable {

    /**
     *
     * 20200405	未开始
     20200406	待验收
     20200407	巡检完成
     20200408	已超时
     20200409	缺勤
     */

    public static final String STATE_WAIT = "20200405";
    public static final String STATE_AUDIT = "20200406";
    public static final String STATE_FINISH = "20200407";
    public static final String STATE_TIMEOUT = "20200408";
    public static final String STATE_NO = "20200409";

    private String sendFlag;
    private String pointEndTime;
    private String inspectionTime;
    private String sortNumber;
    private String machineName;
    private String actUserName;
    private String taskDetailId;
    private String machineId;
    private String pointStartTime;
    private String state;
    private String communityId;
    private String actUserId;
    private String taskId;
    private String planName;
    private String stateName;
    private String planUserName;
    private String standardName;
    private String standardId;
    private String planEndTime;
    private String planInsTime;

    private List<PhotoVo> photos;

    private String description;


    private Date createTime;

    private String statusCd = "0";


    public String getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag;
    }

    public String getPointEndTime() {
        return pointEndTime;
    }

    public void setPointEndTime(String pointEndTime) {
        this.pointEndTime = pointEndTime;
    }

    public String getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(String inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    public String getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(String sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    public String getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(String taskDetailId) {
        this.taskDetailId = taskDetailId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getPointStartTime() {
        return pointStartTime;
    }

    public void setPointStartTime(String pointStartTime) {
        this.pointStartTime = pointStartTime;
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

    public String getPlanUserName() {
        return planUserName;
    }

    public void setPlanUserName(String planUserName) {
        this.planUserName = planUserName;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getPlanInsTime() {
        return planInsTime;
    }

    public void setPlanInsTime(String planInsTime) {
        this.planInsTime = planInsTime;
    }

    public String getStandardId() {
        return standardId;
    }

    public void setStandardId(String standardId) {
        this.standardId = standardId;
    }

    public List<PhotoVo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoVo> photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
