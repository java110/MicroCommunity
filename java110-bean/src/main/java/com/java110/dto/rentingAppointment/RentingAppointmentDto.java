package com.java110.dto.rentingAppointment;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 租赁预约数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RentingAppointmentDto extends PageDto implements Serializable {

    public static final String STATE_SUBMIT = "1001";//提交
    public static final String STATE_SUCCESS = "3003";//租房成功

    private String msg;
    private String tenantTel;
    private String tenantName;
    private String appointmentTime;
    private String appointmentRoomId;
    private String appointmentRoomName;
    private String appointmentId;
    private String remark;
    private String state;
    private String stateName;
    private String tenantSex;
    private String storeId;
    private String roomId;
    private String roomName;
    private String rentingId;

    private String appointmentCommunityId;
    private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTenantTel() {
        return tenantTel;
    }

    public void setTenantTel(String tenantTel) {
        this.tenantTel = tenantTel;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentRoomId() {
        return appointmentRoomId;
    }

    public void setAppointmentRoomId(String appointmentRoomId) {
        this.appointmentRoomId = appointmentRoomId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getTenantSex() {
        return tenantSex;
    }

    public void setTenantSex(String tenantSex) {
        this.tenantSex = tenantSex;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getRentingId() {
        return rentingId;
    }

    public void setRentingId(String rentingId) {
        this.rentingId = rentingId;
    }

    public String getAppointmentRoomName() {
        return appointmentRoomName;
    }

    public void setAppointmentRoomName(String appointmentRoomName) {
        this.appointmentRoomName = appointmentRoomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getAppointmentCommunityId() {
        return appointmentCommunityId;
    }

    public void setAppointmentCommunityId(String appointmentCommunityId) {
        this.appointmentCommunityId = appointmentCommunityId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
