package com.java110.po.attendanceClassesTaskDetail;

import java.io.Serializable;
import java.util.Date;

public class AttendanceClassesTaskDetailPo implements Serializable {

    private String checkTime;
    private String detailId;
    private String specCd;
    private String statusCd = "0";
    private String remark;
    private String facePath;
    private String state;
    private String storeId;
    private String value;
    private String leaveValue;
    private String lateValue;
    private String taskId;



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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
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
}
