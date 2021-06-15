package com.java110.po.systemGoldSetting;

import java.io.Serializable;
import java.util.Date;

public class SystemGoldSettingPo implements Serializable {

    private String buyPrice;
private String goldName;
private String statusCd = "0";
private String goldType;
private String validity;
private String state;
private String usePrice;
private String settingId;
public String getBuyPrice() {
        return buyPrice;
    }
public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }
public String getGoldName() {
        return goldName;
    }
public void setGoldName(String goldName) {
        this.goldName = goldName;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getGoldType() {
        return goldType;
    }
public void setGoldType(String goldType) {
        this.goldType = goldType;
    }
public String getValidity() {
        return validity;
    }
public void setValidity(String validity) {
        this.validity = validity;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getUsePrice() {
        return usePrice;
    }
public void setUsePrice(String usePrice) {
        this.usePrice = usePrice;
    }
public String getSettingId() {
        return settingId;
    }
public void setSettingId(String settingId) {
        this.settingId = settingId;
    }



}
