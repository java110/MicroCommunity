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



}
