package com.java110.dto.couponPool;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 优惠券池数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CouponPoolDto extends PageDto implements Serializable {

    private String buyPrice;
private String couponName;
private String couponStock;
private String actualPrice;
private String validityDay;
private String couponType;
private String couponTypeName;
private String poolId;
private String state;
private String seq;


    private Date createTime;

    private String statusCd = "0";


    public String getBuyPrice() {
        return buyPrice;
    }
public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }
public String getCouponName() {
        return couponName;
    }
public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
public String getCouponStock() {
        return couponStock;
    }
public void setCouponStock(String couponStock) {
        this.couponStock = couponStock;
    }
public String getActualPrice() {
        return actualPrice;
    }
public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }
public String getValidityDay() {
        return validityDay;
    }
public void setValidityDay(String validityDay) {
        this.validityDay = validityDay;
    }
public String getCouponType() {
        return couponType;
    }
public void setCouponType(String couponType) {
        this.couponType = couponType;
    }
public String getPoolId() {
        return poolId;
    }
public void setPoolId(String poolId) {
        this.poolId = poolId;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getSeq() {
        return seq;
    }
public void setSeq(String seq) {
        this.seq = seq;
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

    public String getCouponTypeName() {
        return couponTypeName;
    }

    public void setCouponTypeName(String couponTypeName) {
        this.couponTypeName = couponTypeName;
    }
}
