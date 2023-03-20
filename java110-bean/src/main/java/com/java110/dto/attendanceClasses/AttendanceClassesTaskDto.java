package com.java110.dto.attendanceClasses;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 考勤任务数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AttendanceClassesTaskDto extends PageDto implements Serializable {

    //考勤状态，10000未考勤 20000 考勤中 30000考勤完成
    public static final String STATE_WAIT = "10000";
    public static final String STATE_DOING = "20000";
    public static final String STATE_FINISH = "30000";

    private String classId;
    private String taskMonth;
    private String taskDay;
    private String state;
    private String stateName;
    private String storeId;
    private String taskYear;
    private String taskId;
    private String staffId;
    private String[] staffIds;
    private String staffName;
    private String classesName;
    private String classObjName;


    private Date createTime;

    private String statusCd = "0";

    private String departmentName;
    private String noClockIn;
    private String clockIn;
    private String late;
    private String early;
    private String free;

    List<AttendanceClassesTaskDetailDto> attendanceClassesTaskDetails;

    JSONObject days;


    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTaskMonth() {
        return taskMonth;
    }

    public void setTaskMonth(String taskMonth) {
        this.taskMonth = taskMonth;
    }

    public String getTaskDay() {
        return taskDay;
    }

    public void setTaskDay(String taskDay) {
        this.taskDay = taskDay;
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

    public String getTaskYear() {
        return taskYear;
    }

    public void setTaskYear(String taskYear) {
        this.taskYear = taskYear;
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

    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
    }

    public String getClassObjName() {
        return classObjName;
    }

    public void setClassObjName(String classObjName) {
        this.classObjName = classObjName;
    }

    public List<AttendanceClassesTaskDetailDto> getAttendanceClassesTaskDetails() {
        return attendanceClassesTaskDetails;
    }

    public void setAttendanceClassesTaskDetails(List<AttendanceClassesTaskDetailDto> attendanceClassesTaskDetails) {
        this.attendanceClassesTaskDetails = attendanceClassesTaskDetails;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getNoClockIn() {
        return noClockIn;
    }

    public void setNoClockIn(String noClockIn) {
        this.noClockIn = noClockIn;
    }

    public String getClockIn() {
        return clockIn;
    }

    public void setClockIn(String clockIn) {
        this.clockIn = clockIn;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public String getEarly() {
        return early;
    }

    public void setEarly(String early) {
        this.early = early;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public JSONObject getDays() {
        return days;
    }

    public void setDays(JSONObject days) {
        this.days = days;
    }

    public String[] getStaffIds() {
        return staffIds;
    }

    public void setStaffIds(String[] staffIds) {
        this.staffIds = staffIds;
    }
}
