package com.java110.dto.roomRenovation;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 装修申请数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RoomRenovationDto extends PageDto implements Serializable {

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
    private String stateName;
    private String communityId;
    private String personTel;
    //当前用户id
    private String userId;
    private String isPostpone;
    private String postponeTime;
    private String renovationCompany;
    private String personMain;
    private String personMainTel;
    private String renovationTime;
    private String renovationStartTime;
    private String renovationEndTime;

    private Date createTime;

    private String statusCd = "0";

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getRenovationTime() {
        return renovationTime;
    }

    public void setRenovationTime(String renovationTime) {
        this.renovationTime = renovationTime;
    }

    public String getRenovationStartTime() {
        return renovationStartTime;
    }

    public void setRenovationStartTime(String renovationStartTime) {
        this.renovationStartTime = renovationStartTime;
    }

    public String getRenovationEndTime() {
        return renovationEndTime;
    }

    public void setRenovationEndTime(String renovationEndTime) {
        this.renovationEndTime = renovationEndTime;
    }
}
