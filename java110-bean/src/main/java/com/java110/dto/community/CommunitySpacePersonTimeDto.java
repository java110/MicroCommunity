package com.java110.dto.community;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 场地预约时间数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CommunitySpacePersonTimeDto extends PageDto implements Serializable {

    public static final String STATE_WAIT_CONFIRM = "W"; //待核销
    public static final String STATE_FINISH = "C"; // 核销完成

    public static final String STATE_CL = "CL";//取消预约

    private String spaceId;
    private String cspId;
    private String[] cspIds;
    private String hours;
    private String timeId;
    private String communityId;

    private String appointmentTime;

    private String state;
    private String[] states;


    private Date createTime;

    private String statusCd = "0";


    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getCspId() {
        return cspId;
    }

    public void setCspId(String cspId) {
        this.cspId = cspId;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
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

    public String[] getCspIds() {
        return cspIds;
    }

    public void setCspIds(String[] cspIds) {
        this.cspIds = cspIds;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }
}
