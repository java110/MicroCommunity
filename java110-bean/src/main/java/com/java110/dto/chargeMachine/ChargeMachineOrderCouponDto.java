package com.java110.dto.chargeMachine;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 充电优惠券数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ChargeMachineOrderCouponDto extends PageDto implements Serializable {

    private String couponName;
private String hours;
private String orderId;
private String cmocId;
private String remark;
private String state;
private String couponId;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getCouponName() {
        return couponName;
    }
public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
public String getHours() {
        return hours;
    }
public void setHours(String hours) {
        this.hours = hours;
    }
public String getOrderId() {
        return orderId;
    }
public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
public String getCmocId() {
        return cmocId;
    }
public void setCmocId(String cmocId) {
        this.cmocId = cmocId;
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
}
