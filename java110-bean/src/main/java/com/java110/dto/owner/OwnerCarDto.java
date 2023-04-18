package com.java110.dto.owner;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 车辆管理数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OwnerCarDto extends PageDto implements Serializable {

    public static final String STATE_NORMAL = "1001";
    public static final String STATE_OWE = "2002";
    public static final String STATE_FINISH = "3003";

    public static final String CAR_TYPE_PRIMARY = "1001"; //主车辆
    public static final String CAR_TYPE_MEMBER = "1002"; //车辆成员
    public static final String CAR_TYPE_TEMP = "1003"; //临时车

    public static final String CAR_TYPE_CD_TEMP = "1003";

    public static final String LEASE_TYPE_MONTH = "H"; // 月租车
    public static final String LEASE_TYPE_SALE = "S"; // 出售车
    public static final String LEASE_TYPE_INNER = "I"; //内部车
    public static final String LEASE_TYPE_NO_MONEY = "NM"; //免费车
    public static final String LEASE_TYPE_RESERVE = "R"; //预约车

    public static final String LEASE_TYPE_TEMP = "T";//临时车

    public static final String CAR_TYPE_CREDIT = "9906"; //信用期车牌

    private String carColor;
    private String carBrand;
    private String carType;
    private String carTypeName;
    private String carNum;
    private String carNumLike;
    private String memberCarNum;
    private String memberCarNumLike;
    private String[] carNums;
    private String communityId;
    private String psId;
    private String[] psIds;
    private String[] paIds;
    private String remark;
    private String ownerId;
    private String userId;
    private String carId;
    private String[] carIds;
    private boolean withOwner = false;
    private String ownerName;
    private String idCard;
    private String link;
    private String parkingType;
    //停车场类型
    private String typeCd;
    private String spaceSate;

    private String roomName;

    private Date startTime;
    private Date endTime;
    private String state;
    private String stateName;
    private String iotStateName;
    private String iotRemark;

    private String areaNum;

    private String num;

    private Date createTime;

    private String statusCd = "0";

    private String valid;

    private String bId;
    private String carTypeCd;
    private String carTypeCdName;
    private String[] carTypeCds;
    private String memberId;
    private String unitId;
    private String floorNum;
    private String unitNum;
    private String roomNum;
    private String oweAmount;

    private String leaseType;
    private String[] leaseTypes;

    private String leaseTypeName;

    private String memberCarCount;

    private String userName;

    private String operate;
    private List<OwnerCarAttrDto> ownerCarAttrDto;

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
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

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String[] getCarNums() {
        return carNums;
    }

    public void setCarNums(String[] carNums) {
        this.carNums = carNums;
    }

    public String[] getPsIds() {
        return psIds;
    }

    public void setPsIds(String[] psIds) {
        this.psIds = psIds;
    }

    public boolean isWithOwner() {
        return withOwner;
    }

    public void setWithOwner(boolean withOwner) {
        this.withOwner = withOwner;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(String areaNum) {
        this.areaNum = areaNum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String[] getCarIds() {
        return carIds;
    }

    public void setCarIds(String[] carIds) {
        this.carIds = carIds;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCarTypeCd() {
        return carTypeCd;
    }

    public void setCarTypeCd(String carTypeCd) {
        this.carTypeCd = carTypeCd;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getCarNumLike() {
        return carNumLike;
    }

    public void setCarNumLike(String carNumLike) {
        this.carNumLike = carNumLike;
    }

    public String[] getCarTypeCds() {
        return carTypeCds;
    }

    public void setCarTypeCds(String[] carTypeCds) {
        this.carTypeCds = carTypeCds;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getSpaceSate() {
        return spaceSate;
    }

    public void setSpaceSate(String spaceSate) {
        this.spaceSate = spaceSate;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getOweAmount() {
        return oweAmount;
    }

    public void setOweAmount(String oweAmount) {
        this.oweAmount = oweAmount;
    }

    public List<OwnerCarAttrDto> getOwnerCarAttrDto() {
        return ownerCarAttrDto;
    }

    public void setOwnerCarAttrDto(List<OwnerCarAttrDto> ownerCarAttrDto) {
        this.ownerCarAttrDto = ownerCarAttrDto;
    }

    public String getLeaseType() {
        return leaseType;
    }

    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }

    public String getLeaseTypeName() {
        return leaseTypeName;
    }

    public void setLeaseTypeName(String leaseTypeName) {
        this.leaseTypeName = leaseTypeName;
    }

    public String[] getPaIds() {
        return paIds;
    }

    public void setPaIds(String[] paIds) {
        this.paIds = paIds;
    }

    public String getCarTypeCdName() {
        return carTypeCdName;
    }

    public void setCarTypeCdName(String carTypeCdName) {
        this.carTypeCdName = carTypeCdName;
    }

    public String[] getLeaseTypes() {
        return leaseTypes;
    }

    public void setLeaseTypes(String[] leaseTypes) {
        this.leaseTypes = leaseTypes;
    }

    public String getMemberCarCount() {
        return memberCarCount;
    }

    public void setMemberCarCount(String memberCarCount) {
        this.memberCarCount = memberCarCount;
    }

    public String getMemberCarNum() {
        return memberCarNum;
    }

    public void setMemberCarNum(String memberCarNum) {
        this.memberCarNum = memberCarNum;
    }

    public String getMemberCarNumLike() {
        return memberCarNumLike;
    }

    public void setMemberCarNumLike(String memberCarNumLike) {
        this.memberCarNumLike = memberCarNumLike;
    }

    public String getIotStateName() {
        return iotStateName;
    }

    public void setIotStateName(String iotStateName) {
        this.iotStateName = iotStateName;
    }

    public String getIotRemark() {
        return iotRemark;
    }

    public void setIotRemark(String iotRemark) {
        this.iotRemark = iotRemark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
