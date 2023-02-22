package com.java110.dto.meterMachine;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 智能水电表数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MeterMachineDto extends PageDto implements Serializable {

    private String heartbeatTime;
private String implBean;
private String address;
private String prestoreDegrees;
private String machineName;
private String roomId;
private String roomName;
private String curReadingTime;
private String machineModel;
private String curDegrees;
private String machineId;
private String meterType;
private String feeConfigName;
private String communityId;
private String feeConfigId;


    private Date createTime;

    private String statusCd = "0";


    public String getHeartbeatTime() {
        return heartbeatTime;
    }
public void setHeartbeatTime(String heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }
public String getImplBean() {
        return implBean;
    }
public void setImplBean(String implBean) {
        this.implBean = implBean;
    }
public String getAddress() {
        return address;
    }
public void setAddress(String address) {
        this.address = address;
    }
public String getPrestoreDegrees() {
        return prestoreDegrees;
    }
public void setPrestoreDegrees(String prestoreDegrees) {
        this.prestoreDegrees = prestoreDegrees;
    }
public String getMachineName() {
        return machineName;
    }
public void setMachineName(String machineName) {
        this.machineName = machineName;
    }
public String getRoomId() {
        return roomId;
    }
public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
public String getRoomName() {
        return roomName;
    }
public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
public String getCurReadingTime() {
        return curReadingTime;
    }
public void setCurReadingTime(String curReadingTime) {
        this.curReadingTime = curReadingTime;
    }
public String getMachineModel() {
        return machineModel;
    }
public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
    }
public String getCurDegrees() {
        return curDegrees;
    }
public void setCurDegrees(String curDegrees) {
        this.curDegrees = curDegrees;
    }
public String getMachineId() {
        return machineId;
    }
public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
public String getMeterType() {
        return meterType;
    }
public void setMeterType(String meterType) {
        this.meterType = meterType;
    }
public String getFeeConfigName() {
        return feeConfigName;
    }
public void setFeeConfigName(String feeConfigName) {
        this.feeConfigName = feeConfigName;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getFeeConfigId() {
        return feeConfigId;
    }
public void setFeeConfigId(String feeConfigId) {
        this.feeConfigId = feeConfigId;
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
}
