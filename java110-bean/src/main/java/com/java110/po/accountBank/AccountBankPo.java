package com.java110.po.accountBank;

import java.io.Serializable;
import java.util.Date;

public class AccountBankPo implements Serializable {

    private String personName;
private String bankCode;
private String bankId;
private String bankName;
private String statusCd = "0";
private String shopId;
private String personTel;
public String getPersonName() {
        return personName;
    }
public void setPersonName(String personName) {
        this.personName = personName;
    }
public String getBankCode() {
        return bankCode;
    }
public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
public String getBankId() {
        return bankId;
    }
public void setBankId(String bankId) {
        this.bankId = bankId;
    }
public String getBankName() {
        return bankName;
    }
public void setBankName(String bankName) {
        this.bankName = bankName;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getShopId() {
        return shopId;
    }
public void setShopId(String shopId) {
        this.shopId = shopId;
    }
public String getPersonTel() {
        return personTel;
    }
public void setPersonTel(String personTel) {
        this.personTel = personTel;
    }



}
