package com.java110.dto.user;

import java.io.Serializable;

/**
 * 业主登录返回对象
 */
public class LoginOwnerResDto implements Serializable {

    private String userId;

    private String userName;

    private String ownerId;

    private String memberId;

    private String ownerName;

    private String ownerTel;

    private String communityId;

    private String communityName;

    private String communityTel;


    private String token;

    private String key;

    private String appUserId;

    private String ownerTypeCd;

    private String communityQrCode;


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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getOwnerTypeCd() {
        return ownerTypeCd;
    }

    public void setOwnerTypeCd(String ownerTypeCd) {
        this.ownerTypeCd = ownerTypeCd;
    }

    public String getCommunityTel() {
        return communityTel;
    }

    public void setCommunityTel(String communityTel) {
        this.communityTel = communityTel;
    }

    public String getCommunityQrCode() {
        return communityQrCode;
    }

    public void setCommunityQrCode(String communityQrCode) {
        this.communityQrCode = communityQrCode;
    }
}
