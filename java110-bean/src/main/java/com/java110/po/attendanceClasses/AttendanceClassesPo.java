package com.java110.po.attendanceClasses;

import java.io.Serializable;

public class AttendanceClassesPo implements Serializable {

    private String timeOffset;
    private String clockCount;
    private String classesObjType;
    private String storeId;
    private String clockType;
    private String classesObjId;
    private String classesName;
    private String classesId;
    private String leaveOffset;
    private String lateOffset;
    private String clockTypeValue;
    private String classesObjName;

    private String maxLastOffset;

    private String statusCd = "0";

    public String getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(String timeOffset) {
        this.timeOffset = timeOffset;
    }

    public String getClockCount() {
        return clockCount;
    }

    public void setClockCount(String clockCount) {
        this.clockCount = clockCount;
    }

    public String getClassesObjType() {
        return classesObjType;
    }

    public void setClassesObjType(String classesObjType) {
        this.classesObjType = classesObjType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getClockType() {
        return clockType;
    }

    public void setClockType(String clockType) {
        this.clockType = clockType;
    }

    public String getClassesObjId() {
        return classesObjId;
    }

    public void setClassesObjId(String classesObjId) {
        this.classesObjId = classesObjId;
    }

    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
    }

    public String getClassesId() {
        return classesId;
    }

    public void setClassesId(String classesId) {
        this.classesId = classesId;
    }

    public String getLeaveOffset() {
        return leaveOffset;
    }

    public void setLeaveOffset(String leaveOffset) {
        this.leaveOffset = leaveOffset;
    }

    public String getLateOffset() {
        return lateOffset;
    }

    public void setLateOffset(String lateOffset) {
        this.lateOffset = lateOffset;
    }

    public String getClockTypeValue() {
        return clockTypeValue;
    }

    public void setClockTypeValue(String clockTypeValue) {
        this.clockTypeValue = clockTypeValue;
    }

    public String getClassesObjName() {
        return classesObjName;
    }

    public void setClassesObjName(String classesObjName) {
        this.classesObjName = classesObjName;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getMaxLastOffset() {
        return maxLastOffset;
    }

    public void setMaxLastOffset(String maxLastOffset) {
        this.maxLastOffset = maxLastOffset;
    }
}
