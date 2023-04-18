package com.java110.dto.meterMachine;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 智能水电表数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MeterMachineDto extends PageDto implements Serializable {

    public static final String MACHINE_MODEL_RECHARGE  ="1001";//重置模式
    public static final String MACHINE_MODEL_READ  ="2002";//读表模式

    private String heartbeatTime;
    private String implBean;
    private String implBeanName;
    private String address;
    private String prestoreDegrees;
    private String machineName;
    private String machineNameLike;
    private String roomId;
    private String roomName;
    private String roomNameLike;
    private String curReadingTime;
    private String machineModel;
    private String curDegrees;
    private String machineId;
    private String meterType;
    private String feeConfigName;
    private String feeConfigNameLike;
    private String communityId;
    private String feeConfigId;

    private double rechargeDegree;
    private double rechargeMoney;

    private String degree;

    private List<MeterMachineSpecDto> specs;


    private Date createTime;

    private String statusCd = "0";

    private int readDay ;

    private int readHours;


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

    public List<MeterMachineSpecDto> getSpecs() {
        return specs;
    }

    public void setSpecs(List<MeterMachineSpecDto> specs) {
        this.specs = specs;
    }

    public String getImplBeanName() {
        return implBeanName;
    }

    public void setImplBeanName(String implBeanName) {
        this.implBeanName = implBeanName;
    }

    public String getMachineNameLike() {
        return machineNameLike;
    }

    public void setMachineNameLike(String machineNameLike) {
        this.machineNameLike = machineNameLike;
    }

    public String getRoomNameLike() {
        return roomNameLike;
    }

    public void setRoomNameLike(String roomNameLike) {
        this.roomNameLike = roomNameLike;
    }

    public String getFeeConfigNameLike() {
        return feeConfigNameLike;
    }

    public void setFeeConfigNameLike(String feeConfigNameLike) {
        this.feeConfigNameLike = feeConfigNameLike;
    }

    public double getRechargeDegree() {
        return rechargeDegree;
    }

    public void setRechargeDegree(double rechargeDegree) {
        this.rechargeDegree = rechargeDegree;
    }

    public int getReadDay() {
        return readDay;
    }

    public void setReadDay(int readDay) {
        this.readDay = readDay;
    }

    public int getReadHours() {
        return readHours;
    }

    public void setReadHours(int readHours) {
        this.readHours = readHours;
    }

    public double getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(double rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
