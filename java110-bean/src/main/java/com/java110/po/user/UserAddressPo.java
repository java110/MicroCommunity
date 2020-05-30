package com.java110.po.user;

import java.io.Serializable;

/**
 * @ClassName UserAddressPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 22:43
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class UserAddressPo implements Serializable {

    private String id;
    private String addressId;

    private String userId;
    private String tel;
    private String postalCode;
    private String address;
    private String isDefault;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
