package com.java110.vo.api.machineRecord;

import java.io.Serializable;
import java.util.Date;

public class ApiMachineRecordDataVo implements Serializable {

    private String machineRecordId;
    private String machineCode;
    private String machineId;
    private String name;
    private String openTypeCd;
    private String openTypeName;
    private String tel;
    private String idCard;
    private String recordTypeCd;
    private String createTime;

    private String faceUrl;

    private String machineName;
    private String locationTypeCd;

    private String locationObjName;
    private String communityId;


    public String getMachineRecordId() {
        return machineRecordId;
    }

    public void setMachineRecordId(String machineRecordId) {
        this.machineRecordId = machineRecordId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenTypeCd() {
        return openTypeCd;
    }

    public void setOpenTypeCd(String openTypeCd) {
        this.openTypeCd = openTypeCd;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRecordTypeCd() {
        return recordTypeCd;
    }

    public void setRecordTypeCd(String recordTypeCd) {
        this.recordTypeCd = recordTypeCd;
    }

    public String getOpenTypeName() {
        return openTypeName;
    }

    public void setOpenTypeName(String openTypeName) {
        this.openTypeName = openTypeName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getLocationTypeCd() {
        return locationTypeCd;
    }

    public void setLocationTypeCd(String locationTypeCd) {
        this.locationTypeCd = locationTypeCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getLocationObjName() {
        return locationObjName;
    }

    public void setLocationObjName(String locationObjName) {
        this.locationObjName = locationObjName;
    }
}
