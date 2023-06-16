package com.java110.dto.classes;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 排班数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ScheduleClassesDto extends PageDto implements Serializable {

    public static final String STATE_START = "1001";
    public static final String STATE_STOP = "2002";

    //排班类型 1001 按日排班 2002 按周排班 3003 按月排班
    public static final String SCHEDULE_TYPE_DAY = "1001";
    public static final String SCHEDULE_TYPE_WEEK = "2002";
    public static final String SCHEDULE_TYPE_MONTH = "3003";

    private String scheduleType;
    private String scheduleTypeName;
    private String scheduleCycle;
    private String computeTime;
    private String name;
    private String nameLike;
    private String remark;
    private String state;
    private String stateName;
    private String storeId;
    private String scheduleId;
    private long staffCount;


    private Date createTime;

    private String statusCd = "0";


    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleCycle() {
        return scheduleCycle;
    }

    public void setScheduleCycle(String scheduleCycle) {
        this.scheduleCycle = scheduleCycle;
    }

    public String getComputeTime() {
        return computeTime;
    }

    public void setComputeTime(String computeTime) {
        this.computeTime = computeTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
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

    public String getScheduleTypeName() {
        return scheduleTypeName;
    }

    public void setScheduleTypeName(String scheduleTypeName) {
        this.scheduleTypeName = scheduleTypeName;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public long getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(long staffCount) {
        this.staffCount = staffCount;
    }
}
