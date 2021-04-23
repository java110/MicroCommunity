package com.java110.vo.api.inspectionPoint;

import java.io.Serializable;

public class ApiInspectionPointDataVo implements Serializable {

    private String inspectionRouteId;
    private String inspectionId;
    private String inspectionName;
    private String remark;
    private String machineCode;
    private String machineName;
    private String locationTypeCd;
    private String locationTypeName;
    private String locationObjId;
    private String locationObjName;
    private String pointObjType;
    private String pointObjId;
    private String pointObjName;
    private String machineId;
    private String communityId;


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
}
