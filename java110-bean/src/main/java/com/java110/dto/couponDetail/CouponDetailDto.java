package com.java110.dto.couponDetail;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 商家购买记录表数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CouponDetailDto extends PageDto implements Serializable {

    private String buyPrice;
private String couponName;
private String amount;
private String buyCount;
private String actualPrice;
private String validityDay;
private String poolId;
private String detailId;
private String shopId;
private String shopName;
private String shopNameLike;
private String orderId;


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
public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getBuyCount() {
        return buyCount;
    }
public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
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
public String getPoolId() {
        return poolId;
    }
public void setPoolId(String poolId) {
        this.poolId = poolId;
    }
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getShopId() {
        return shopId;
    }
public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopNameLike() {
        return shopNameLike;
    }

    public void setShopNameLike(String shopNameLike) {
        this.shopNameLike = shopNameLike;
    }
}
