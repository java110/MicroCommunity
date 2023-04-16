package com.java110.dto.couponPool;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 商家优惠券池数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CouponShopPoolDto extends PageDto implements Serializable {

    private String couponName;
private String actualPrice;
private String validityDay;
private String poolId;
private String shopId;
private String stock;
private String spId;


    private Date createTime;

    private String statusCd = "0";


    public String getCouponName() {
        return couponName;
    }
public void setCouponName(String couponName) {
        this.couponName = couponName;
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
public String getShopId() {
        return shopId;
    }
public void setShopId(String shopId) {
        this.shopId = shopId;
    }
public String getStock() {
        return stock;
    }
public void setStock(String stock) {
        this.stock = stock;
    }
public String getSpId() {
        return spId;
    }
public void setSpId(String spId) {
        this.spId = spId;
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
