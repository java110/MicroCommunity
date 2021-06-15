package com.java110.po.shopVipAccount;

import java.io.Serializable;
import java.util.Date;

public class ShopVipAccountPo implements Serializable {

    private String amount;
private String vipAcctId;
private String vipId;
private String acctType;
private String statusCd = "0";
private String shopId;
private String acctName;
private String storeId;
public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getVipAcctId() {
        return vipAcctId;
    }
public void setVipAcctId(String vipAcctId) {
        this.vipAcctId = vipAcctId;
    }
public String getVipId() {
        return vipId;
    }
public void setVipId(String vipId) {
        this.vipId = vipId;
    }
public String getAcctType() {
        return acctType;
    }
public void setAcctType(String acctType) {
        this.acctType = acctType;
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
public String getAcctName() {
        return acctName;
    }
public void setAcctName(String acctName) {
        this.acctName = acctName;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }



}
