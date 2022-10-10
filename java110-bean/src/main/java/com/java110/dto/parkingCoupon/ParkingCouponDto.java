package com.java110.dto.parkingCoupon;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 停车卷数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingCouponDto extends PageDto implements Serializable {

    public static final String TYPE_CD_HOURS = "1001";
    public static final String TYPE_CD_MONEY = "2002";
    public static final String TYPE_CD_DISCOUNT = "3003";
    public static final String TYPE_CD_FREE = "4004";


    private String typeCd;
    private String typeCdName;
    private String name;
    private String paId;
    private String paName;
    private String couponId;
    private String communityId;
    private String value;
    private String valuePrice;


    private Date createTime;

    private String statusCd = "0";


    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
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
}
