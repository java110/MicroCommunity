package com.java110.dto.attendanceClasses;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 考勤班次数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AttendanceClassesDto extends PageDto implements Serializable {

    public static final String CLASSES_OBJ_TYPE_PARTMENT = "1003";

    private String timeOffset;
    private String clockCount;
    private String classesObjType;
    private String storeId;
    private String clockType;
    private String clockTypeName;
    private String classesObjId;
    private String[] classesObjIds;
    private String classesName;
    private String classesId;
    private String leaveOffset;
    private String lateOffset;

    private String maxLastOffset;

    private String clockTypeValue;
    private String classesObjName;
    private List<AttendanceClassesAttrDto> attrs;

    private Date createTime;

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

    public String getClassesObjName() {
        return classesObjName;
    }

    public void setClassesObjName(String classesObjName) {
        this.classesObjName = classesObjName;
    }

    public List<AttendanceClassesAttrDto> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<AttendanceClassesAttrDto> attrs) {
        this.attrs = attrs;
    }

    public String getClockTypeName() {
        return clockTypeName;
    }

    public void setClockTypeName(String clockTypeName) {
        this.clockTypeName = clockTypeName;
    }

    public String[] getClassesObjIds() {
        return classesObjIds;
    }

    public void setClassesObjIds(String[] classesObjIds) {
        this.classesObjIds = classesObjIds;
    }

    public String getMaxLastOffset() {
        return maxLastOffset;
    }

    public void setMaxLastOffset(String maxLastOffset) {
        this.maxLastOffset = maxLastOffset;
    }
}
