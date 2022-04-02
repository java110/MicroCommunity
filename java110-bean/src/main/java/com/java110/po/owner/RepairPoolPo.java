package com.java110.po.owner;

import java.io.Serializable;

/**
 * @ClassName RepairPoolPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 14:21
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class RepairPoolPo implements Serializable {

    private String repairId;
    private String communityId;
    private String repairType;
    private String repairName;
    private String tel;
    private String roomId;
    private String appointmentTime;
    private String context;
    private String state;
    private String repairObjType;
    private String repairObjId;
    private String repairObjName;
    private String maintenanceType;
    private String repairChannel;
    private String bId;
    private String statusCd = "0";
    private String repairMaterials;
    private String repairFee;
    private String payType;

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String getRepairObjType() {
        return repairObjType;
    }

    public void setRepairObjType(String repairObjType) {
        this.repairObjType = repairObjType;
    }

    public String getRepairObjId() {
        return repairObjId;
    }

    public void setRepairObjId(String repairObjId) {
        this.repairObjId = repairObjId;
    }

    public String getRepairObjName() {
        return repairObjName;
    }

    public void setRepairObjName(String repairObjName) {
        this.repairObjName = repairObjName;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getRepairChannel() {
        return repairChannel;
    }

    public void setRepairChannel(String repairChannel) {
        this.repairChannel = repairChannel;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getRepairMaterials() {
        return repairMaterials;
    }

    public void setRepairMaterials(String repairMaterials) {
        this.repairMaterials = repairMaterials;
    }

    public String getRepairFee() {
        return repairFee;
    }

    public void setRepairFee(String repairFee) {
        this.repairFee = repairFee;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
