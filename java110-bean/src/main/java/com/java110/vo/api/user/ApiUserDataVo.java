package com.java110.vo.api.user;

import java.io.Serializable;

public class ApiUserDataVo implements Serializable {

    private String userId;

    private String userName;

    private String name;

    private String tel;

    private String email;

    private String address;

    private String password;

    private String locationCd;

    private String openId;

    private String mallOpenId;

    private String key;

    private int age;

    private String sex;

    private String levelCd;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMallOpenId() {
        return mallOpenId;
    }

    public void setMallOpenId(String mallOpenId) {
        this.mallOpenId = mallOpenId;
    }
}
