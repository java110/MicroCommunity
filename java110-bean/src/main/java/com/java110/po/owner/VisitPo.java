package com.java110.po.owner;

import java.io.Serializable;

/**
 * @ClassName VisitPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 13:39
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class VisitPo implements Serializable {
    private String vId;
    private String vName;
    private String visitGender;
    private String phoneNumber;
    private String userId;
    private String communityId;
    private String ownerId;
    private String visitCase;
    private String visitTime;
    private String freeTime;
    private String departureTime;
    private String statusCd = "0";
    private String carNum;
    private String entourage;
    private String reasonType;
    private String state;
    private String stateName;
    private String stateRemark;
    private String carState;
    private String carStateRemark;
    private String carStateName;
    private String psId;
    private String flag;
    private String recordState;
    private String sign;
    private String roomId;
    private String mark;

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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getEntourage() {
        return entourage;
    }

    public void setEntourage(String entourage) {
        this.entourage = entourage;
    }

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateRemark() {
        return stateRemark;
    }

    public void setStateRemark(String stateRemark) {
        this.stateRemark = stateRemark;
    }

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
    }

    public String getRecordState() {
        return recordState;
    }

    public void setRecordState(String recordState) {
        this.recordState = recordState;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCarState() {
        return carState;
    }

    public void setCarState(String carState) {
        this.carState = carState;
    }

    public String getCarStateRemark() {
        return carStateRemark;
    }

    public void setCarStateRemark(String carStateRemark) {
        this.carStateRemark = carStateRemark;
    }

    public String getCarStateName() {
        return carStateName;
    }

    public void setCarStateName(String carStateName) {
        this.carStateName = carStateName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
