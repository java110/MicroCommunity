package com.java110.dto.couponPropertyPool;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 优惠券数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CouponPropertyPoolDto extends PageDto implements Serializable {

    private String couponName;
private String toType;
private String fromType;
private String validityDay;
private String cppId;
private String communityName;
private String state;
private String communityId;
private String stock;
private String fromId;


    private Date createTime;

    private String statusCd = "0";


    public String getCouponName() {
        return couponName;
    }
public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
public String getToType() {
        return toType;
    }
public void setToType(String toType) {
        this.toType = toType;
    }
public String getFromType() {
        return fromType;
    }
public void setFromType(String fromType) {
        this.fromType = fromType;
    }
public String getValidityDay() {
        return validityDay;
    }
public void setValidityDay(String validityDay) {
        this.validityDay = validityDay;
    }
public String getCppId() {
        return cppId;
    }
public void setCppId(String cppId) {
        this.cppId = cppId;
    }
public String getCommunityName() {
        return communityName;
    }
public void setCommunityName(String communityName) {
        this.communityName = communityName;
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
public String getStock() {
        return stock;
    }
public void setStock(String stock) {
        this.stock = stock;
    }
public String getFromId() {
        return fromId;
    }
public void setFromId(String fromId) {
        this.fromId = fromId;
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
}
