package com.java110.dto;

import com.java110.dto.fee.FeeDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 小区房屋数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RoomDto extends PageDto implements Serializable {

    public static final String STATE_SELL = "2001"; // 已入住
    public static final String STATE_FREE = "2002"; //未销售
    public static final String STATE_DELIVERY = "2003";//已交房
    public static final String STATE_NO_HOME = "2004";//未入住
    public static final String STATE_RENOVATION = "2005";//已装修
    public static final String STATE_SHOP_FIRE = "2006";//已出租
    public static final String STATE_SHOP_SELL = "2007";//已售
    public static final String STATE_SHOP_FREE = "2008";//空闲
    public static final String STATE_SHOP_REPAIR = "2009";//装修中
    public static final String ROOM_TYPE_ROOM = "1010301";//普通房屋
    public static final String ROOM_TYPE_SHOPS = "2020602";//商铺

    public static final String ROOM_SUB_TYPE_PERSON = "110";
    public static final String ROOM_SUB_TYPE_WORK = "119";
    public static final String ROOM_SUB_TYPE_HOUSE = "120";

    private String feeCoefficient;
    private String section;
    private String remark;
    private String userId;
    private String roomId;
    private String[] roomIds;
    private String layer;
    private String[] layers;
    private String builtUpArea;
    private String roomNum;
    private String roomNumLike;

    private String unitId;
    private String[] unitIds;
    private String unitArea;
    private String apartment;
    private String apartmentName;
    private String communityId;
    private String floorId;
    private String[] floorIds;
    private String floorNum;
    private String floorArea;
    private String userName;
    private String ownerId;
    private String ownerName;
    private String ownerNameLike;
    private String idCard;
    private String link;
    private String roomType;
    private String roomSubType;
    private String roomSubTypeName;
    private String roomRent;
    private String roomArea;
    private String oweAmount;

    private String state;
    private String[] states;
    private String stateName;
    private String unitNum;

    private List<RoomAttrDto> roomAttrDto;

    private List<FeeDto> fees;

    private Date createTime;
    private Date startTime;
    private Date endTime;

    private String roomName;

    private String statusCd = "0";

    private String ownerTel; // 这个程序用，对外不输出

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<RoomAttrDto> getRoomAttrDto() {
        return roomAttrDto;
    }

    public void setRoomAttrDto(List<RoomAttrDto> roomAttrDto) {
        this.roomAttrDto = roomAttrDto;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String[] getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(String[] roomIds) {
        this.roomIds = roomIds;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
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

    public String getUnitArea() {
        return unitArea;
    }

    public void setUnitArea(String unitArea) {
        this.unitArea = unitArea;
    }

    public String getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(String floorArea) {
        this.floorArea = floorArea;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public List<FeeDto> getFees() {
        return fees;
    }

    public void setFees(List<FeeDto> fees) {
        this.fees = fees;
    }

    public String[] getLayers() {
        return layers;
    }

    public void setLayers(String[] layers) {
        this.layers = layers;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOweAmount() {
        return oweAmount;
    }

    public void setOweAmount(String oweAmount) {
        this.oweAmount = oweAmount;
    }

    public String[] getFloorIds() {
        return floorIds;
    }

    public void setFloorIds(String[] floorIds) {
        this.floorIds = floorIds;
    }

    public String getRoomNumLike() {
        return roomNumLike;
    }

    public void setRoomNumLike(String roomNumLike) {
        this.roomNumLike = roomNumLike;
    }

    public String getOwnerNameLike() {
        return ownerNameLike;
    }

    public void setOwnerNameLike(String ownerNameLike) {
        this.ownerNameLike = ownerNameLike;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String[] getUnitIds() {
        return unitIds;
    }

    public void setUnitIds(String[] unitIds) {
        this.unitIds = unitIds;
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

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }
}
