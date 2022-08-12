package com.java110.po.repair;

import java.io.Serializable;

public class RepairTypeUserPo implements Serializable {

    private String typeUserId;
    private String repairType;
    private String remark;
    private String state;
    private String staffName;
    private String communityId;
    private String staffId;
    private String statusCd = "0";

    public String getTypeUserId() {
        return typeUserId;
    }

    public void setTypeUserId(String typeUserId) {
        this.typeUserId = typeUserId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
