package com.java110.dto.workTask;

import com.java110.dto.PageDto;
import com.java110.dto.workPool.WorkPoolDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 工作单任务数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WorkTaskDto extends WorkPoolDto implements Serializable {

    public static final String STATE_WAIT = "W";// W 待处理 D 处理中 C 处理完成
    public static final String STATE_DOING = "D"; //  W 待处理 D 处理中 C 处理完成
    public static final String STATE_COMPLETE = "C";// W 待处理 D 处理中 C 处理完成

    private String staffName;
    private String startTime;
    private String state;
    private String stateName;
    private String endTime;

    private String finishTime;
    private String communityId;
    private String storeId;
    private String taskId;
    private String workId;
    private String[] workIds;
    private String staffId;

    private String staffNameLike;
    private String queryStartTime;
    private String queryEndTime;

    private String createUserNameLike;

    private String taskTimeout;

    private String taskInsTime;

    private String orgStaffId;
    private String orgStaffName;

    private Date createTime;

    private String statusCd = "0";

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public String[] getWorkIds() {
        return workIds;
    }

    public void setWorkIds(String[] workIds) {
        this.workIds = workIds;
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

    public String getQueryStartTime() {
        return queryStartTime;
    }

    public void setQueryStartTime(String queryStartTime) {
        this.queryStartTime = queryStartTime;
    }

    public String getQueryEndTime() {
        return queryEndTime;
    }

    public void setQueryEndTime(String queryEndTime) {
        this.queryEndTime = queryEndTime;
    }

    public String getCreateUserNameLike() {
        return createUserNameLike;
    }

    public void setCreateUserNameLike(String createUserNameLike) {
        this.createUserNameLike = createUserNameLike;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getTaskTimeout() {
        return taskTimeout;
    }

    public void setTaskTimeout(String taskTimeout) {
        this.taskTimeout = taskTimeout;
    }

    public String getTaskInsTime() {
        return taskInsTime;
    }

    public void setTaskInsTime(String taskInsTime) {
        this.taskInsTime = taskInsTime;
    }

    public String getOrgStaffId() {
        return orgStaffId;
    }

    public void setOrgStaffId(String orgStaffId) {
        this.orgStaffId = orgStaffId;
    }

    public String getOrgStaffName() {
        return orgStaffName;
    }

    public void setOrgStaffName(String orgStaffName) {
        this.orgStaffName = orgStaffName;
    }
}
