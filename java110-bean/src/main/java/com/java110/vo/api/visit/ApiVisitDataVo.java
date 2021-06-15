package com.java110.vo.api.visit;

import java.io.Serializable;
import java.util.Date;

public class ApiVisitDataVo implements Serializable {
    private String vId;
    private String vName;
    private String visitGender;
    private String phoneNumber;
    private String userId;
    private String communityId;
    private String ownerId;
    private String visitCase;
    private String visitTime;
    private String departureTime;
    private String createTime;
    private String statusCd = "0";

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getVisitGender() {
        return visitGender;
    }

    public void setVisitGender(String visitGender) {
        this.visitGender = visitGender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getVisitCase() {
        return visitCase;
    }

    public void setVisitCase(String visitCase) {
        this.visitCase = visitCase;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
