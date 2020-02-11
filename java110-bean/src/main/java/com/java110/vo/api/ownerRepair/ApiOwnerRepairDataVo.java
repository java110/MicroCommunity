package com.java110.vo.api.ownerRepair;

import java.io.Serializable;
import java.util.Date;

public class ApiOwnerRepairDataVo implements Serializable {

    private String repairId;
    private String repairType;
    private String repairName;
    private String tel;
    private String roomId;
    private String appointmentTime;
    private String context;
    private String state;
    private String stateName;
    private String repairTypeName;

    private String staffId;

    private String userId;
    private String userName;

    private String repairDispatchState;
    private String repairDispatchContext;
    private String repairDispatchStateName;

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getRepairName() {
        return repairName;
    }

    public void setRepairName(String repairName) {
        this.repairName = repairName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getRepairTypeName() {
        return repairTypeName;
    }

    public void setRepairTypeName(String repairTypeName) {
        this.repairTypeName = repairTypeName;
    }


    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRepairDispatchState() {
        return repairDispatchState;
    }

    public void setRepairDispatchState(String repairDispatchState) {
        this.repairDispatchState = repairDispatchState;
    }

    public String getRepairDispatchContext() {
        return repairDispatchContext;
    }

    public void setRepairDispatchContext(String repairDispatchContext) {
        this.repairDispatchContext = repairDispatchContext;
    }

    public String getRepairDispatchStateName() {
        return repairDispatchStateName;
    }

    public void setRepairDispatchStateName(String repairDispatchStateName) {
        this.repairDispatchStateName = repairDispatchStateName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
