package com.java110.po.room;

import java.io.Serializable;

/**
 * @ClassName RoomPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 22:01
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class RoomPo implements Serializable {

    private String roomId;

    private String roomNum;
    private String unitId;
    private String layer;
    private String section;
    private String apartment;
    private String builtUpArea;
    private String feeCoefficient;
    private String userId;
    private String remark;
    private String state;
    private String communityId;
    private String roomType;
    private String statusCd = "0";
    private String roomSubType;
    private String roomRent;
    private String roomArea;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getBuiltUpArea() {
        return builtUpArea;
    }

    public void setBuiltUpArea(String builtUpArea) {
        this.builtUpArea = builtUpArea;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getFeeCoefficient() {
        return feeCoefficient;
    }

    public void setFeeCoefficient(String feeCoefficient) {
        this.feeCoefficient = feeCoefficient;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getRoomSubType() {
        return roomSubType;
    }

    public void setRoomSubType(String roomSubType) {
        this.roomSubType = roomSubType;
    }

    public String getRoomRent() {
        return roomRent;
    }

    public void setRoomRent(String roomRent) {
        this.roomRent = roomRent;
    }

    public String getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(String roomArea) {
        this.roomArea = roomArea;
    }

}
