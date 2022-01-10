package com.java110.dto.onlinePay;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 线上支付数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OnlinePayDto extends PageDto implements Serializable {

    //状态 W待支付 C 支付完成 F 通知失败 WT 待退费 CT退费完成
    public static final String STATE_WAIT = "W";
    public static final String STATE_PAY_FAIL = "PF"; // 支付失败
    public static final String STATE_COMPILE = "C";
    public static final String STATE_FAIL = "F";
    public static final String STATE_WT = "WT"; // 待退费
    public static final String STATE_CT = "CT"; // 退费完成
    public static final String STATE_FT = "FT"; // 退费失败

    private String refundFee;
    private String mchId;
    private String orderId;
    private String totalFee;
    private String openId;
    private String appId;
    private String payId;
    private String state;
    private String message;
    private String payName;
    private String transactionId;


    private Date createTime;

    private String statusCd = "0";


    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
