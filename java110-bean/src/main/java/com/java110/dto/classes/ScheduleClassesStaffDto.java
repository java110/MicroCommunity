package com.java110.dto.classes;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 排班员工数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ScheduleClassesStaffDto extends PageDto implements Serializable {

    private String staffName;
    private String staffNameLike;
    private String storeId;
    private String scsId;
    private String scheduleId;
    private String[] scheduleIds;
    private String staffId;

    private Date today;

    private boolean isWork;

    private long staffCount;

    private String curDate;

    private String orgId;

    List<ScheduleClassesDayDto> days;

    List<ScheduleClassesTimeDto> times;


    private Date createTime;

    private String statusCd = "0";


    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getScsId() {
        return scsId;
    }

    public void setScsId(String scsId) {
        this.scsId = scsId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
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

    public String[] getScheduleIds() {
        return scheduleIds;
    }

    public void setScheduleIds(String[] scheduleIds) {
        this.scheduleIds = scheduleIds;
    }

    public long getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(long staffCount) {
        this.staffCount = staffCount;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public boolean isWork() {
        return isWork;
    }

    public void setWork(boolean work) {
        isWork = work;
    }

    public List<ScheduleClassesDayDto> getDays() {
        return days;
    }

    public void setDays(List<ScheduleClassesDayDto> days) {
        this.days = days;
    }

    public String getStaffNameLike() {
        return staffNameLike;
    }

    public void setStaffNameLike(String staffNameLike) {
        this.staffNameLike = staffNameLike;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public List<ScheduleClassesTimeDto> getTimes() {
        return times;
    }

    public void setTimes(List<ScheduleClassesTimeDto> times) {
        this.times = times;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
