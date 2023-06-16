package com.java110.dto.classes;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 排班天数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ScheduleClassesDayDto extends PageDto implements Serializable {

    public static final String WORKDAY_NO = "2002";

    private String workday;
    private String workdayName;
    private String dayId;
    private String day;
    private String scheduleId;
    private String weekFlag;

    private List<ScheduleClassesTimeDto> times;


    private Date createTime;

    private String statusCd = "0";


    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getWeekFlag() {
        return weekFlag;
    }

    public void setWeekFlag(String weekFlag) {
        this.weekFlag = weekFlag;
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

    public List<ScheduleClassesTimeDto> getTimes() {
        return times;
    }

    public void setTimes(List<ScheduleClassesTimeDto> times) {
        this.times = times;
    }

    public String getWorkdayName() {
        return workdayName;
    }

    public void setWorkdayName(String workdayName) {
        this.workdayName = workdayName;
    }
}
