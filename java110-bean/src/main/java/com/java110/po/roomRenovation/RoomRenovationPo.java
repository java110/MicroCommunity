package com.java110.po.roomRenovation;

import java.io.Serializable;
import java.util.List;

public class RoomRenovationPo implements Serializable {

    private String statusCd = "0";
    private String remark;
    private String isViolation;
    private String rId;
    private String roomId;
    private String roomName;
    private String personName;
    private String violationDesc;
    private String startTime;
    private String endTime;
    private String state;
    private String communityId;
    private String personTel;
    private String examineRemark;
    private List<String> photos;
    private String videoName;
    private String isTrue;
    private String isTrueName;
    private String isPostpone;
    private String postponeTime;
    private String renovationCompany;
    private String personMain;
    private String personMainTel;

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsViolation() {
        return isViolation;
    }

    public void setIsViolation(String isViolation) {
        this.isViolation = isViolation;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getViolationDesc() {
        return violationDesc;
    }

    public void setViolationDesc(String violationDesc) {
        this.violationDesc = violationDesc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getPersonTel() {
        return personTel;
    }

    public void setPersonTel(String personTel) {
        this.personTel = personTel;
    }

    public String getExamineRemark() {
        return examineRemark;
    }

    public void setExamineRemark(String examineRemark) {
        this.examineRemark = examineRemark;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(String isTrue) {
        this.isTrue = isTrue;
    }

    public String getIsTrueName() {
        return isTrueName;
    }

    public void setIsTrueName(String isTrueName) {
        this.isTrueName = isTrueName;
    }

    public String getIsPostpone() {
        return isPostpone;
    }

    public void setIsPostpone(String isPostpone) {
        this.isPostpone = isPostpone;
    }

    public String getPostponeTime() {
        return postponeTime;
    }

    public void setPostponeTime(String postponeTime) {
        this.postponeTime = postponeTime;
    }

    public String getRenovationCompany() {
        return renovationCompany;
    }

    public void setRenovationCompany(String renovationCompany) {
        this.renovationCompany = renovationCompany;
    }

    public String getPersonMain() {
        return personMain;
    }

    public void setPersonMain(String personMain) {
        this.personMain = personMain;
    }

    public String getPersonMainTel() {
        return personMainTel;
    }

    public void setPersonMainTel(String personMainTel) {
        this.personMainTel = personMainTel;
    }
}
