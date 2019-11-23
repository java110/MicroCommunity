package com.java110.dto;

import java.io.Serializable;

/**
 * @ClassName UserDto
 * @Description 查询 用户信息
 * @Author wuxw
 * @Date 2019/4/24 14:43
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class UserDto extends PageDto implements Serializable {

    private String userId;

    private String userName;

    private String name;

    private String tel;

    private String email;

    private String address;

    private String password;

    private String locationCd;

    private int age;

    private String sex;

    private String levelCd;

    private String storeId;

    private String orgName;

    private String parentOrgId;

    private String orgId;

    private String staffName;

    private String openId;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocationCd() {
        return locationCd;
    }

    public void setLocationCd(String locationCd) {
        this.locationCd = locationCd;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLevelCd() {
        return levelCd;
    }

    public void setLevelCd(String levelCd) {
        this.levelCd = levelCd;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
