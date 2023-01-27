package com.java110.vo.api;

import com.java110.dto.RoomAttrDto;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ApiRoomVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/5/8 0:26
 * @Version 1.0
 * add by wuxw 2019/5/8
 **/
public class ApiRoomDataVo implements Serializable {

    private String feeCoefficient;
    private String section;
    private String remark;
    private String userName;
    private String roomId;
    private String layer;
    private String builtUpArea;
    private String roomNum;
    private String unitId;
    private String unitNum;
    private String floorId;
    private String floorNum;
    private String state;
    private String stateName;
    private String apartment;
    private String apartmentName;
    private String roomType;
    private String roomSubType;
    private String roomRent;
    private String roomArea;
    private String roomSubTypeName;
    private String ownerId;
    private String ownerName;
    private String idCard;
    private String link;
    private String startTime;
    private String endTime;

    private String roomName;

    private List<RoomAttrDto> roomAttrDto;


    private String memberCount;
    private String carCount;
    private String roomCount;
    private String complaintCount;
    private String repairCount;
    private String roomOweFee;
    private String oweFee;

    private String contractCount;


    public String getFeeCoefficient() {
        return feeCoefficient;
    }

    public void setFeeCoefficient(String feeCoefficient) {
        this.feeCoefficient = feeCoefficient;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getBuiltUpArea() {
        return builtUpArea;
    }

    public void setBuiltUpArea(String builtUpArea) {
        this.builtUpArea = builtUpArea;
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

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<RoomAttrDto> getRoomAttrDto() {
        return roomAttrDto;
    }

    public void setRoomAttrDto(List<RoomAttrDto> roomAttrDto) {
        this.roomAttrDto = roomAttrDto;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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

    public String getRoomSubTypeName() {
        return roomSubTypeName;
    }

    public void setRoomSubTypeName(String roomSubTypeName) {
        this.roomSubTypeName = roomSubTypeName;
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }

    public String getCarCount() {
        return carCount;
    }

    public void setCarCount(String carCount) {
        this.carCount = carCount;
    }

    public String getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(String roomCount) {
        this.roomCount = roomCount;
    }

    public String getComplaintCount() {
        return complaintCount;
    }

    public void setComplaintCount(String complaintCount) {
        this.complaintCount = complaintCount;
    }

    public String getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(String repairCount) {
        this.repairCount = repairCount;
    }

    public String getRoomOweFee() {
        return roomOweFee;
    }

    public void setRoomOweFee(String roomOweFee) {
        this.roomOweFee = roomOweFee;
    }

    public String getOweFee() {
        return oweFee;
    }

    public void setOweFee(String oweFee) {
        this.oweFee = oweFee;
    }

    public String getContractCount() {
        return contractCount;
    }

    public void setContractCount(String contractCount) {
        this.contractCount = contractCount;
    }
}
