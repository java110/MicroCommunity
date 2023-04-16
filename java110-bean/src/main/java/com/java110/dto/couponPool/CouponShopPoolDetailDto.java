package com.java110.dto.couponPool;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 商家赠送记录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CouponShopPoolDetailDto extends PageDto implements Serializable {

    private String couponName;
private String actualPrice;
private String validityDay;
private String detailId;
private String sendCount;
private String userName;
private String createUserName;
private String spId;
private String userId;
private String createUserId;
private String poolId;
private String tel;
private String shopId;
private String orderId;


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
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getSendCount() {
        return sendCount;
    }
public void setSendCount(String sendCount) {
        this.sendCount = sendCount;
    }
public String getUserName() {
        return userName;
    }
public void setUserName(String userName) {
        this.userName = userName;
    }
public String getSpId() {
        return spId;
    }
public void setSpId(String spId) {
        this.spId = spId;
    }
public String getUserId() {
        return userId;
    }
public void setUserId(String userId) {
        this.userId = userId;
    }
public String getPoolId() {
        return poolId;
    }
public void setPoolId(String poolId) {
        this.poolId = poolId;
    }
public String getTel() {
        return tel;
    }
public void setTel(String tel) {
        this.tel = tel;
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

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
}
