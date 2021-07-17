package com.java110.po.accountBondObj;

import java.io.Serializable;
import java.util.Date;

public class AccountBondObjPo implements Serializable {

    private String bondId;
private String bobjId;
private String objId;
private String objName;
private String bondType;
private String receivableAmount;
private String remark;
private String startTime;
private String statusCd = "0";
private String receivedAmount;
private String state;
private String endTime;
public String getBondId() {
        return bondId;
    }
public void setBondId(String bondId) {
        this.bondId = bondId;
    }
public String getBobjId() {
        return bobjId;
    }
public void setBobjId(String bobjId) {
        this.bobjId = bobjId;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getBondType() {
        return bondType;
    }
public void setBondType(String bondType) {
        this.bondType = bondType;
    }
public String getReceivableAmount() {
        return receivableAmount;
    }
public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
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
public String getReceivedAmount() {
        return receivedAmount;
    }
public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getEndTime() {
        return endTime;
    }
public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }
}
