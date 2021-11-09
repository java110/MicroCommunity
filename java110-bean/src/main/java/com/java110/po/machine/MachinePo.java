package com.java110.po.machine;

import java.io.Serializable;

/**
 * @ClassName MachinePo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 8:55
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class MachinePo implements Serializable {

    private String machineId;
    private String machineCode;
    private String machineVersion;
    private String machineTypeCd;
    private String communityId;
    private String machineName;
    private String authCode;
    private String machineIp;
    private String machineMac;
    private String locationTypeCd;
    private String locationObjId;
    private String state;
    private String direction;
    private String typeId;
    private String bId;
    private String heartbeatTime;
    private String statusCd = "0";


    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineVersion() {
        return machineVersion;
    }

    public void setMachineVersion(String machineVersion) {
        this.machineVersion = machineVersion;
    }

    public String getMachineTypeCd() {
        return machineTypeCd;
    }

    public void setMachineTypeCd(String machineTypeCd) {
        this.machineTypeCd = machineTypeCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getMachineIp() {
        return machineIp;
    }

    public void setMachineIp(String machineIp) {
        this.machineIp = machineIp;
    }

    public String getMachineMac() {
        return machineMac;
    }

    public void setMachineMac(String machineMac) {
        this.machineMac = machineMac;
    }

    public String getLocationTypeCd() {
        return locationTypeCd;
    }

    public void setLocationTypeCd(String locationTypeCd) {
        this.locationTypeCd = locationTypeCd;
    }

    public String getLocationObjId() {
        return locationObjId;
    }

    public void setLocationObjId(String locationObjId) {
        this.locationObjId = locationObjId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(String heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }
}
