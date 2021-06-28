package com.java110.po.accountBond;

import java.io.Serializable;
import java.util.Date;

public class AccountBondPo implements Serializable {

    private String amount;
private String bondId;
private String bondMonth;
private String bondType;
private String objId;
private String remark;
private String statusCd = "0";
private String bondName;
public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getBondId() {
        return bondId;
    }
public void setBondId(String bondId) {
        this.bondId = bondId;
    }
public String getBondMonth() {
        return bondMonth;
    }
public void setBondMonth(String bondMonth) {
        this.bondMonth = bondMonth;
    }
public String getBondType() {
        return bondType;
    }
public void setBondType(String bondType) {
        this.bondType = bondType;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getBondName() {
        return bondName;
    }
public void setBondName(String bondName) {
        this.bondName = bondName;
    }



}
