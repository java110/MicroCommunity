package com.java110.po.repair;

import java.io.Serializable;

public class RepairSettingPo implements Serializable {

    private String repairTypeName;
private String repairType;
private String remark;
private String communityId;
private String repairWay;
private String settingId;
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



}
