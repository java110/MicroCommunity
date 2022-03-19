package com.java110.vo.api.inspectionPoint;

import java.io.Serializable;

public class ApiInspectionPointDataVo implements Serializable {

    private String inspectionRouteId;
    private String inspectionId;
    private String irpRelId;
    private String inspectionName;
    private String remark;
    private String machineCode;
    private String machineName;
    private String locationTypeCd;
    private String locationTypeName;
    private String locationObjId;
    private String locationObjName;
    private String pointObjType;
    private String pointTypeName;
    private String pointObjId;
    private String pointObjName;
    private String machineId;
    private String communityId;
    private String pointStartTime;
    private String pointEndTime;
    private String sortNumber;
    private String itemId;
    private String itemName;
    private String nfcCode;

    private String url;

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
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

    public String getLocationObjId() {
        return locationObjId;
    }

    public void setLocationObjId(String locationObjId) {
        this.locationObjId = locationObjId;
    }

    public String getLocationObjName() {
        return locationObjName;
    }

    public void setLocationObjName(String locationObjName) {
        this.locationObjName = locationObjName;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getLocationTypeName() {
        return locationTypeName;
    }

    public void setLocationTypeName(String locationTypeName) {
        this.locationTypeName = locationTypeName;
    }

    public String getInspectionRouteId() {
        return inspectionRouteId;
    }

    public void setInspectionRouteId(String inspectionRouteId) {
        this.inspectionRouteId = inspectionRouteId;
    }

    public String getPointObjType() {
        return pointObjType;
    }

    public void setPointObjType(String pointObjType) {
        this.pointObjType = pointObjType;
    }

    public String getPointObjId() {
        return pointObjId;
    }

    public void setPointObjId(String pointObjId) {
        this.pointObjId = pointObjId;
    }

    public String getPointObjName() {
        return pointObjName;
    }

    public void setPointObjName(String pointObjName) {
        this.pointObjName = pointObjName;
    }

    public String getPointTypeName() {
        return pointTypeName;
    }

    public void setPointTypeName(String pointTypeName) {
        this.pointTypeName = pointTypeName;
    }

    public String getPointStartTime() {
        return pointStartTime;
    }

    public void setPointStartTime(String pointStartTime) {
        this.pointStartTime = pointStartTime;
    }

    public String getPointEndTime() {
        return pointEndTime;
    }

    public void setPointEndTime(String pointEndTime) {
        this.pointEndTime = pointEndTime;
    }

    public String getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(String sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getIrpRelId() {
        return irpRelId;
    }

    public void setIrpRelId(String irpRelId) {
        this.irpRelId = irpRelId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getNfcCode() {
        return nfcCode;
    }

    public void setNfcCode(String nfcCode) {
        this.nfcCode = nfcCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
