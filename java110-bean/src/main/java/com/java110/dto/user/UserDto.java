package com.java110.dto.user;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName UserDto
 * @Description 查询 用户信息
 * @Author wuxw
 * @Date 2019/4/24 14:43
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class UserDto extends PageDto implements Serializable {

    public static final String LEVEL_CD_MALL = "03";//商城用户
    public static final String LEVEL_CD_ADMIN = "00";//系统管理员
    public static final String LEVEL_CD_STAFF = "01";//员工
    public static final String LEVEL_CD_USER = "02";//普通用户

    private String userId;
    private String[] userIds;

    private String userName;

    private String name;

    private String nameLike;

    private String tel;

    private String email;

    private String address;

    private String password;

    private String locationCd;

    private int age;

    private String sex;

    private String levelCd;
    private String[] levelCds;

    private String storeId;

    private String orgName;

    private String parentOrgId;

    private String departmentOrgId;

    private String orgId;

    private String[] orgIds;

    private String staffName;

    private String staffId;

    private String openId;
    private String unionId;
    private String mallOpenId;

    private String statusCd = "0";

    private String token;

    private String key;//临时登录秘钥，每次登录后重置

    private String parentOrgName;

    private List<UserAttrDto> userAttrs;

    private String bId;

    private String belongCommunityId;

    private boolean loginByPhone;

    private String relCd;

    private String  relCdName;

    private String score;

    private String userFlag;

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

    public List<UserAttrDto> getUserAttrs() {
        return userAttrs;
    }

    public void setUserAttrs(List<UserAttrDto> userAttrs) {
        this.userAttrs = userAttrs;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
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

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getBelongCommunityId() {
        return belongCommunityId;
    }

    public void setBelongCommunityId(String belongCommunityId) {
        this.belongCommunityId = belongCommunityId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLoginByPhone() {
        return loginByPhone;
    }

    public void setLoginByPhone(boolean loginByPhone) {
        this.loginByPhone = loginByPhone;
    }

    public String getDepartmentOrgId() {
        return departmentOrgId;
    }

    public void setDepartmentOrgId(String departmentOrgId) {
        this.departmentOrgId = departmentOrgId;
    }

    public String getRelCd() {
        return relCd;
    }

    public void setRelCd(String relCd) {
        this.relCd = relCd;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(String userFlag) {
        this.userFlag = userFlag;
    }

    public String[] getLevelCds() {
        return levelCds;
    }

    public void setLevelCds(String[] levelCds) {
        this.levelCds = levelCds;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getMallOpenId() {
        return mallOpenId;
    }

    public void setMallOpenId(String mallOpenId) {
        this.mallOpenId = mallOpenId;
    }

    public String getRelCdName() {
        return relCdName;
    }

    public void setRelCdName(String relCdName) {
        this.relCdName = relCdName;
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

    public String[] getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String[] orgIds) {
        this.orgIds = orgIds;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }
}
