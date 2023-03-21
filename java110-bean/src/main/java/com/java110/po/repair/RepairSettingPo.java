package com.java110.po.repair;

import java.io.Serializable;

public class RepairSettingPo implements Serializable {
    private String repairTypeName;
    private String repairType;
    private String remark;
    private String communityId;
    private String repairWay;
    private String settingId;
    private String publicArea;
    private String payFeeFlag;
    private String priceScope;
    private String returnVisitFlag;
    private String repairSettingType;
    private String statusCd = "0";
    private String isShow;

    public String getRepairTypeName() {
        return repairTypeName;
    }

    public void setRepairTypeName(String repairTypeName) {
        this.repairTypeName = repairTypeName;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRepairWay() {
        return repairWay;
    }

    public void setRepairWay(String repairWay) {
        this.repairWay = repairWay;
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public String getPublicArea() {
        return publicArea;
    }

    public void setPublicArea(String publicArea) {
        this.publicArea = publicArea;
    }

    public String getPayFeeFlag() {
        return payFeeFlag;
    }

    public void setPayFeeFlag(String payFeeFlag) {
        this.payFeeFlag = payFeeFlag;
    }

    public String getPriceScope() {
        return priceScope;
    }

    public void setPriceScope(String priceScope) {
        this.priceScope = priceScope;
    }

    public String getReturnVisitFlag() {
        return returnVisitFlag;
    }

    public void setReturnVisitFlag(String returnVisitFlag) {
        this.returnVisitFlag = returnVisitFlag;
    }

    public String getRepairSettingType() {
        return repairSettingType;
    }

    public void setRepairSettingType(String repairSettingType) {
        this.repairSettingType = repairSettingType;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
}
