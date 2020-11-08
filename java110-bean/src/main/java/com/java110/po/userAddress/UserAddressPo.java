package com.java110.po.userAddress;

import java.io.Serializable;
import java.util.Date;

public class UserAddressPo implements Serializable {

    private String areaCode;
private String isDefault;
private String address;
private String tel;
private String statusCd = "0";
private String userId;
private String addressId;
public String getAreaCode() {
        return areaCode;
    }
public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
public String getIsDefault() {
        return isDefault;
    }
public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
public String getAddress() {
        return address;
    }
public void setAddress(String address) {
        this.address = address;
    }
public String getTel() {
        return tel;
    }
public void setTel(String tel) {
        this.tel = tel;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getUserId() {
        return userId;
    }
public void setUserId(String userId) {
        this.userId = userId;
    }
public String getAddressId() {
        return addressId;
    }
public void setAddressId(String addressId) {
        this.addressId = addressId;
    }



}
