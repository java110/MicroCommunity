package com.java110.dto.hardwareAdapation;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 设备数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MachineDto extends PageDto implements Serializable {

    private String machineMac;
    private String machineId;
    private String machineCode;
    private String authCode;
    private String machineVersion;
    private String communityId;
    private String machineName;
    private String machineTypeCd;
    private String machineTypeCdName;
    private String machineIp;
    private String bId;

    private Date createTime;

    private String statusCd = "0";


    public String getMachineMac() {
        return machineMac;
    }

    public void setMachineMac(String machineMac) {
        this.machineMac = machineMac;
    }

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

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getMachineVersion() {
        return machineVersion;
    }

    public void setMachineVersion(String machineVersion) {
        this.machineVersion = machineVersion;
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

    public String getMachineTypeCd() {
        return machineTypeCd;
    }

    public void setMachineTypeCd(String machineTypeCd) {
        this.machineTypeCd = machineTypeCd;
    }

    public String getMachineIp() {
        return machineIp;
    }

    public void setMachineIp(String machineIp) {
        this.machineIp = machineIp;
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

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getMachineTypeCdName() {
        return machineTypeCdName;
    }

    public void setMachineTypeCdName(String machineTypeCdName) {
        this.machineTypeCdName = machineTypeCdName;
    }
}
