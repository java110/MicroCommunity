package com.java110.po.groupBuySetting;

import java.io.Serializable;

public class GroupBuySettingPo implements Serializable {

    private String groupBuyName;
    private String groupBuyDesc;
    private String validHours;
    private String startTime;
    private String statusCd = "0";
    private String endTime;
    private String storeId;
    private String settingId;

    public String getGroupBuyName() {
        return groupBuyName;
    }

    public void setGroupBuyName(String groupBuyName) {
        this.groupBuyName = groupBuyName;
    }

    public String getGroupBuyDesc() {
        return groupBuyDesc;
    }

    public void setGroupBuyDesc(String groupBuyDesc) {
        this.groupBuyDesc = groupBuyDesc;
    }

    public String getValidHours() {
        return validHours;
    }

    public void setValidHours(String validHours) {
        this.validHours = validHours;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }


}
