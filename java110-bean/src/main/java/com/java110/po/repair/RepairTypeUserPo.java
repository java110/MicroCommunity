package com.java110.po.repair;

import java.io.Serializable;

public class RepairTypeUserPo implements Serializable {

    private String typeUserId;
    private String repairType;
    private String remark;
    private String state;
    private String userName;
    private String communityId;
    private String userId;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
