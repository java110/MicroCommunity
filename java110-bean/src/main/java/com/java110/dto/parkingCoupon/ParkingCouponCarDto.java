package com.java110.dto.parkingCoupon;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 车辆停车卷数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingCouponCarDto extends PageDto implements Serializable {

    //状态 1001 未使用 2002 已使用 3003 作废
    public static final String STATE_WAIT = "1001";
    public static final String STATE_FINISH = "2002";

    private String giveWay;
    private String giveWayName;

    private String carNum;
    private String carNumLike;
    private String couponShopId;
    private String remark;
    private String couponId;
    private String couponName;
    private String pccId;
    private String typeCd;
    private String typeCdName;
    private String paId;
    private String paName;
    private String startTime;
    private String shopId;
    private String shopName;
    private String shopNameLike;
    private String state;
    private String stateName;
    private String endTime;
    private String communityId;
    private String value;


    private Date createTime;

    private String statusCd = "0";


    public String getGiveWay() {
        return giveWay;
    }

    public void setGiveWay(String giveWay) {
        this.giveWay = giveWay;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCouponShopId() {
        return couponShopId;
    }

    public void setCouponShopId(String couponShopId) {
        this.couponShopId = couponShopId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPccId() {
        return pccId;
    }

    public void setPccId(String pccId) {
        this.pccId = pccId;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getTypeCdName() {
        return typeCdName;
    }

    public void setTypeCdName(String typeCdName) {
        this.typeCdName = typeCdName;
    }

    public String getPaName() {
        return paName;
    }

    public void setPaName(String paName) {
        this.paName = paName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCarNumLike() {
        return carNumLike;
    }

    public void setCarNumLike(String carNumLike) {
        this.carNumLike = carNumLike;
    }

    public String getShopNameLike() {
        return shopNameLike;
    }

    public void setShopNameLike(String shopNameLike) {
        this.shopNameLike = shopNameLike;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getGiveWayName() {
        return giveWayName;
    }

    public void setGiveWayName(String giveWayName) {
        this.giveWayName = giveWayName;
    }
}
