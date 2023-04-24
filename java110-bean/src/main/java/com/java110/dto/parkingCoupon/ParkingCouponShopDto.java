package com.java110.dto.parkingCoupon;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 商家停车卷数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingCouponShopDto extends PageDto implements Serializable {



    private String quantity;
    private String paName;
    private String paId;
    private String shopName;
    private String couponShopId;
    private String communityName;
    private String startTime;
    private String shopId;
    private String[] shopIds;
    private String endTime;
    private String couponId;
    private String couponName;
    private String communityId;


    private Date createTime;

    private String statusCd = "0";

    // typeCd,pc.`value`,pc.value_price valuePrice,td.`name` typeCdName

    private String typeCd;
    private String value;
    private String valuePrice;
    private String typeCdName;


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPaName() {
        return paName;
    }

    public void setPaName(String paName) {
        this.paName = paName;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCouponShopId() {
        return couponShopId;
    }

    public void setCouponShopId(String couponShopId) {
        this.couponShopId = couponShopId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String[] getShopIds() {
        return shopIds;
    }

    public void setShopIds(String[] shopIds) {
        this.shopIds = shopIds;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValuePrice() {
        return valuePrice;
    }

    public void setValuePrice(String valuePrice) {
        this.valuePrice = valuePrice;
    }

    public String getTypeCdName() {
        return typeCdName;
    }

    public void setTypeCdName(String typeCdName) {
        this.typeCdName = typeCdName;
    }
}
