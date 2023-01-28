package com.java110.dto.parking;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 停车位数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingSpaceDto extends PageDto implements Serializable {

    public static final String STATE_FREE = "F";
    public static final String STATE_HIRE = "H";
    public static final String STATE_SELL = "S";

    public static final String NUM_MOTHER = "母";

    public static final String TYPE_CD_COMMON = "1";
    public static final String TYPE_CD_SON_MOTHER = "2";

    private String area;
    private String typeCd;
    private String typeCdName;
    private String num;
    private String[] nums;
    private String psId;
    private String paId;
    private String remark;
    private String state;
    private String stateName;
    private String communityId;
    private String userId;
    private String areaNum;
    private String carNum;
    private String ownerId;
    private String ownerName;
    private String idCard;
    private String link;
    private String parkingType;
    private String parkingTypeName;

    private String[] psIds;
    private String[] paIds;

    private String[] states;


    private Date createTime;

    private String statusCd = "0";
    private boolean withOwnerCar = false;


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }


    public String[] getPsIds() {
        return psIds;
    }

    public void setPsIds(String[] psIds) {
        this.psIds = psIds;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getTypeCdName() {
        return typeCdName;
    }

    public void setTypeCdName(String typeCdName) {
        this.typeCdName = typeCdName;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(String areaNum) {
        this.areaNum = areaNum;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
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

    public boolean isWithOwnerCar() {
        return withOwnerCar;
    }

    public void setWithOwnerCar(boolean withOwnerCar) {
        this.withOwnerCar = withOwnerCar;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getParkingTypeName() {
        return parkingTypeName;
    }

    public void setParkingTypeName(String parkingTypeName) {
        this.parkingTypeName = parkingTypeName;
    }

    public String[] getPaIds() {
        return paIds;
    }

    public void setPaIds(String[] paIds) {
        this.paIds = paIds;
    }

    public String[] getNums() {
        return nums;
    }

    public void setNums(String[] nums) {
        this.nums = nums;
    }
}
