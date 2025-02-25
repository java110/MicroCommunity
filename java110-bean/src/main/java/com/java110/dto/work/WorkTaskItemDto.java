package com.java110.dto.work;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 工作任务明细数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WorkTaskItemDto extends WorkPoolContentDto implements Serializable {

    private String deductionPersonId;
    private String finishTime;
    private String deductionPersonName;
    private String deductionPersonNameLike;
    private String deductionMoney;
    private String contentId;
    private String storeId;
    private String workId;
    private String deductionReason;
    private String itemId;
    private String state;

    private String[] states;
    private String stateName;
    private String communityId;
    private String taskId;

    private String staffName;
    private String staffNameLike;

    private String remark;

    private String pathUrl;

    private String startTime;

    private String endTime;


    private Date createTime;

    private String statusCd = "0";

    private String score;



    public String getDeductionPersonId() {
        return deductionPersonId;
    }

    public void setDeductionPersonId(String deductionPersonId) {
        this.deductionPersonId = deductionPersonId;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getDeductionPersonName() {
        return deductionPersonName;
    }

    public void setDeductionPersonName(String deductionPersonName) {
        this.deductionPersonName = deductionPersonName;
    }

    public String getDeductionMoney() {
        return deductionMoney;
    }

    public void setDeductionMoney(String deductionMoney) {
        this.deductionMoney = deductionMoney;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getDeductionReason() {
        return deductionReason;
    }

    public void setDeductionReason(String deductionReason) {
        this.deductionReason = deductionReason;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStaffNameLike() {
        return staffNameLike;
    }

    public void setStaffNameLike(String staffNameLike) {
        this.staffNameLike = staffNameLike;
    }

    public String getDeductionPersonNameLike() {
        return deductionPersonNameLike;
    }

    public void setDeductionPersonNameLike(String deductionPersonNameLike) {
        this.deductionPersonNameLike = deductionPersonNameLike;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }
}
