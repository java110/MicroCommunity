package com.java110.vo.api.staff;

import java.io.Serializable;
import java.util.List;

public class ApiStaffDataVo implements Serializable {

    private String userId;

    private String userName;
    private String initials;
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

    private String parentOrgName;

    private String orgLevel;

    private String staffId;

    private String staffName;

    private String relCd;

    private String relCdName;

    private String parentTwoOrgId;

    private String relId;

    private List<String> urls;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRelCd() {
        return relCd;
    }

    public void setRelCd(String relCd) {
        this.relCd = relCd;
    }

    public String getRelCdName() {
        return relCdName;
    }

    public void setRelCdName(String relCdName) {
        this.relCdName = relCdName;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getParentTwoOrgId() {
        return parentTwoOrgId;
    }

    public void setParentTwoOrgId(String parentTwoOrgId) {
        this.parentTwoOrgId = parentTwoOrgId;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }
}
