package com.java110.dto.importData;

import java.io.Serializable;

/**
 * {"machineId":"","machineName":"1-1-1智能水表","address":"1212","meterType":"102023062029980006","machineModel":"2002","roomId":"75202305221015270788000231",
 * "roomName":"1-1-1001","feeConfigId":"922023062058050004",
 * "implBean":"1","readDay":"1","readHours":"1",
 * "communityId":"2023052267100146"
 * }
 */
public class ImportMeterMachineDto implements Serializable {

    private String machineName;
    private String address;
    private String meterType;
    private String machineModel;
    private String roomName;
    private String feeName;
    private String implBean;

    private String communityId;

    private String value1;

    private String userId;

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getImplBean() {
        return implBean;
    }

    public void setImplBean(String implBean) {
        this.implBean = implBean;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
