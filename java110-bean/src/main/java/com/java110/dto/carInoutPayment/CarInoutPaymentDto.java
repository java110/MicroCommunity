package com.java110.dto.carInoutPayment;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 车辆支付数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CarInoutPaymentDto extends PageDto implements Serializable {

    public static final String PAY_TYPE_CRASH = "1";
    public static final String PAY_TYPE_WECHAT = "2";
    public static final String PAY_TYPE_ALIPAY = "1";

    private String realCharge;
    private String realChargeTotal;
    private String inoutId;
    private String payType;
    private String paymentId;
    private String paId;
    private String[] paIds;
    private String communityId;
    private String payCharge;
    private String payChargeTotal;
    private String payTime;
    private String carNum;
    private String payTypeName;
    private String stateName;
    private String inTime;

    private String boxId;


    private Date createTime;

    private String statusCd = "0";

    private String startTime;
    private String endTime;

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getRealCharge() {
        return realCharge;
    }

    public void setRealCharge(String realCharge) {
        this.realCharge = realCharge;
    }

    public String getInoutId() {
        return inoutId;
    }

    public void setInoutId(String inoutId) {
        this.inoutId = inoutId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getPayCharge() {
        return payCharge;
    }

    public void setPayCharge(String payCharge) {
        this.payCharge = payCharge;
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

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String[] getPaIds() {
        return paIds;
    }

    public void setPaIds(String[] paIds) {
        this.paIds = paIds;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRealChargeTotal() {
        return realChargeTotal;
    }

    public void setRealChargeTotal(String realChargeTotal) {
        this.realChargeTotal = realChargeTotal;
    }

    public String getPayChargeTotal() {
        return payChargeTotal;
    }

    public void setPayChargeTotal(String payChargeTotal) {
        this.payChargeTotal = payChargeTotal;
    }
}
