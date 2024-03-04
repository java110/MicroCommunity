package com.java110.dto.workPool;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 工作单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WorkPoolDto extends PageDto implements Serializable {

    public static final String WORK_CYCLE_ONE = "1001"; // 一次性工单
    public static final String WORK_CYCLE_CYCLE = "2002"; // 周期性工单
    public static final String STATE_WAIT = "W"; //  W 待处理 D 处理中 C处理完成
    public static final String STATE_DOING = "D"; //  W 待处理 D 处理中 C处理完成
    public static final String STATE_COMPLETE = "C"; //  W 待处理 D 处理中 C处理完成

    private String workCycle;
    private String createUserId;
    private String createUserName;
    private String storeId;
    private String workName;
    private String workId;
    private String wtId;
    private String typeName;

    private String createUserTel;
    private String startTime;
    private String endTime;
    private String state;

    private String stateName;

    private String communityId;

    private String curStaffName;

    private String curCopyName;

    private Date createTime;

    private String statusCd = "0";

    private String queryEndTime;

    private String queryStartTime;

    private String content;

    private String pathUrl;

    private String taskId;

    private String staffId;

    private String copyId;

    private String remark;

    private String staffNameLike;

    private String workNameLike;

    private String period;

    private String hours;

    private String beforeTime;

    private String periodMonth;

    private String periodDay;

    private String periodWorkday;

    public String getWorkCycle() {
        return workCycle;
    }

    public void setWorkCycle(String workCycle) {
        this.workCycle = workCycle;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getWtId() {
        return wtId;
    }

    public void setWtId(String wtId) {
        this.wtId = wtId;
    }

    public String getCreateUserTel() {
        return createUserTel;
    }

    public void setCreateUserTel(String createUserTel) {
        this.createUserTel = createUserTel;
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

    public String getCurStaffName() {
        return curStaffName;
    }

    public void setCurStaffName(String curStaffName) {
        this.curStaffName = curStaffName;
    }

    public String getCurCopyName() {
        return curCopyName;
    }

    public void setCurCopyName(String curCopyName) {
        this.curCopyName = curCopyName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getQueryEndTime() {
        return queryEndTime;
    }

    public void setQueryEndTime(String queryEndTime) {
        this.queryEndTime = queryEndTime;
    }

    public String getQueryStartTime() {
        return queryStartTime;
    }

    public void setQueryStartTime(String queryStartTime) {
        this.queryStartTime = queryStartTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getCopyId() {
        return copyId;
    }

    public void setCopyId(String copyId) {
        this.copyId = copyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStaffNameLike() {
        return staffNameLike;
    }

    public void setStaffNameLike(String staffNameLike) {
        this.staffNameLike = staffNameLike;
    }

    public String getWorkNameLike() {
        return workNameLike;
    }

    public void setWorkNameLike(String workNameLike) {
        this.workNameLike = workNameLike;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(String beforeTime) {
        this.beforeTime = beforeTime;
    }

    public String getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(String periodMonth) {
        this.periodMonth = periodMonth;
    }

    public String getPeriodDay() {
        return periodDay;
    }

    public void setPeriodDay(String periodDay) {
        this.periodDay = periodDay;
    }

    public String getPeriodWorkday() {
        return periodWorkday;
    }

    public void setPeriodWorkday(String periodWorkday) {
        this.periodWorkday = periodWorkday;
    }

}
