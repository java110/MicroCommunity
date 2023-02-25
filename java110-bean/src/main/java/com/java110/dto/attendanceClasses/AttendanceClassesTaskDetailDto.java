package com.java110.dto.attendanceClasses;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 考勤任务明细数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AttendanceClassesTaskDetailDto extends PageDto implements Serializable {

    //打卡状态 10000未考勤  30000正常考勤 40000 迟到 50000早退 60000 免考勤 70000 补考勤
    public static final String STATE_REPLENISH = "70000";//补考勤
    public static final String STATE_WAIT = "10000";//补考勤
    public static final String STATE_NORMAL = "30000";//正常考勤
    public static final String STATE_LATE = "40000";//迟到
    public static final String STATE_LEAVE = "50000";//早退

    public static final String SPEC_CD_START = "1001" ;//上班时间
    public static final String SPEC_CD_END = "2002" ;//上班时间

    private String checkTime;
    private String nowCheckTime;
    private String detailId;
    private String specCd;
    private String specName;
    private String remark;
    private String facePath;
    private String state;

    private String[] states;

    private String stateName;
    private String storeId;
    private String value;
    private String taskId;



    private String classId;

    private String staffId;

    private String staffName;

    private String[] taskIds;


    private Date createTime;

    private String statusCd = "0";


    private String leaveValue;
    private String lateValue;

    private String startTime;

    private String endTime;

    private String taskDay;


    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getSpecCd() {
        return specCd;
    }

    public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFacePath() {
        return facePath;
    }

    public void setFacePath(String facePath) {
        this.facePath = facePath;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String[] getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(String[] taskIds) {
        this.taskIds = taskIds;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
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

    public String getLeaveValue() {
        return leaveValue;
    }

    public void setLeaveValue(String leaveValue) {
        this.leaveValue = leaveValue;
    }

    public String getLateValue() {
        return lateValue;
    }

    public void setLateValue(String lateValue) {
        this.lateValue = lateValue;
    }

    public String getNowCheckTime() {
        return nowCheckTime;
    }

    public void setNowCheckTime(String nowCheckTime) {
        this.nowCheckTime = nowCheckTime;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }


    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getTaskDay() {
        return taskDay;
    }

    public void setTaskDay(String taskDay) {
        this.taskDay = taskDay;
    }
}
