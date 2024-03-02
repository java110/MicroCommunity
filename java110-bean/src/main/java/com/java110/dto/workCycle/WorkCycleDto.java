package com.java110.dto.workCycle;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 工作单周期数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WorkCycleDto extends PageDto implements Serializable {

    public static final String PERIOD_MONTH_DAY = "2020022"; // 月天
    public static final String PERIOD_MONTH_WORKDAY = "2020023"; // 按周
    private String workCycle;
    private String period;
    private String beforeTime;
    private String cycleId;
    private String periodWorkday;
    private String hours;
    private String storeId;
    private String workId;
    private String staffName;
    private String periodMonth;
    private String communityId;
    private String staffId;
    private String periodDay;

    private String curTime;

    private Date createTime;

    private String[] workIds;

    private String statusCd = "0";

    public String getWorkCycle() {
        return workCycle;
    }

    public void setWorkCycle(String workCycle) {
        this.workCycle = workCycle;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(String beforeTime) {
        this.beforeTime = beforeTime;
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public String getPeriodWorkday() {
        return periodWorkday;
    }

    public void setPeriodWorkday(String periodWorkday) {
        this.periodWorkday = periodWorkday;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(String periodMonth) {
        this.periodMonth = periodMonth;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getPeriodDay() {
        return periodDay;
    }

    public void setPeriodDay(String periodDay) {
        this.periodDay = periodDay;
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

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getCurTime() {
        return curTime;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public String[] getWorkIds() {
        return workIds;
    }

    public void setWorkIds(String[] workIds) {
        this.workIds = workIds;
    }
}
