package com.java110.dto.visit;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @ClassName FloorDto
 * @Description 访客信息数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class VisitDto extends PageDto implements Serializable {

    //访客记录审核状态 0表示未审核；1表示审核通过；2表示审核拒绝
    public static final String STATE_W = "0"; //待审核
    public static final String STATE_C = "1"; //表示审核通过
    public static final String STATE_F = "2"; //表示审核拒绝
    public static final String STATE_D = "3"; // 审核中

    //车辆审核状态 0表示未审核；1表示审核通过；2表示审核拒绝
    public static final String CAR_STATE_W = "0"; //待审核
    public static final String CAR_STATE_C = "1"; //表示审核通过
    public static final String CAR_STATE_F = "2"; //表示审核拒绝
    public static final String CAR_STATE_D = "3"; // 审核中

    private String departureTime;
    private String vName;
    private String vNameLike;
    private String visitGender;
    private String ownerId;
    private String ownerName;
    private String createUserId;
    private String vId;
    private String[] vIds;

    private String visitTime;
    private String phoneNumber;
    private String visitCase;
    private String communityId;
    private String bId;
    private String visitStartTime;
    private String freeTime;
    private String visitEndTime;
    private String departureEndTime;
    private String statusCd = "0";
    private String carNum;
    private String entourage;
    private String reasonType;

    private String reasonTypeName;
    private String state;
    private String stateName;
    private String stateRemark;
    private String carState;
    private String carStateRemark;
    private String carStateName;
    private String createTime;
    private String sameDay;
    private String carNumNoEmpty;
    private String psId;
    private String recordState;
    private String parkingAreaName;
    private String parkingSpaceNum;
    private String parkAreaNum;
    private String flag;
    private String sign;
    private String fileRelName;
    private String fileSaveName;

    private String flowId;
    private String roomId;

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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVisitCase() {
        return visitCase;
    }

    public void setVisitCase(String visitCase) {
        this.visitCase = visitCase;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getVisitStartTime() {
        return visitStartTime;
    }

    public void setVisitStartTime(String visitStartTime) {
        this.visitStartTime = visitStartTime;
    }

    public String getVisitEndTime() {
        return visitEndTime;
    }

    public void setVisitEndTime(String visitEndTime) {
        this.visitEndTime = visitEndTime;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSameDay() {
        return sameDay;
    }

    public void setSameDay(String sameDay) {
        this.sameDay = sameDay;
    }

    public String getCarNumNoEmpty() {
        return carNumNoEmpty;
    }

    public void setCarNumNoEmpty(String carNumNoEmpty) {
        this.carNumNoEmpty = carNumNoEmpty;
    }

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
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

    public String getParkingAreaName() {
        return parkingAreaName;
    }

    public void setParkingAreaName(String parkingAreaName) {
        this.parkingAreaName = parkingAreaName;
    }

    public String getParkingSpaceNum() {
        return parkingSpaceNum;
    }

    public void setParkingSpaceNum(String parkingSpaceNum) {
        this.parkingSpaceNum = parkingSpaceNum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDepartureEndTime() {
        return departureEndTime;
    }

    public void setDepartureEndTime(String departureEndTime) {
        this.departureEndTime = departureEndTime;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFileRelName() {
        return fileRelName;
    }

    public void setFileRelName(String fileRelName) {
        this.fileRelName = fileRelName;
    }

    public String getFileSaveName() {
        return fileSaveName;
    }

    public void setFileSaveName(String fileSaveName) {
        this.fileSaveName = fileSaveName;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String[] getvIds() {
        return vIds;
    }

    public void setvIds(String[] vIds) {
        this.vIds = vIds;
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

    public String getParkAreaNum() {
        return parkAreaNum;
    }

    public void setParkAreaNum(String parkAreaNum) {
        this.parkAreaNum = parkAreaNum;
    }

    public String getReasonTypeName() {
        return reasonTypeName;
    }

    public void setReasonTypeName(String reasonTypeName) {
        this.reasonTypeName = reasonTypeName;
    }

    public String getvNameLike() {
        return vNameLike;
    }

    public void setvNameLike(String vNameLike) {
        this.vNameLike = vNameLike;
    }
}
