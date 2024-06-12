package com.java110.dto.payment;

import com.java110.dto.integral.GiftIntegralDto;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * 支付订单信息
 * 封装实体类
 */
public class PaymentOrderDto implements Serializable{

    public static final String SHARE_ACCT = "Y";

    private String orderId;

    private String transactionId;

    //金额
    private double money;

    private String name;

    private String appId;

    private String openId;

    private String userId;

    private String isShare;

    private GiftIntegralDto giftIntegralDto;


    private ResponseEntity<String> responseEntity;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ResponseEntity<String> getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity<String> responseEntity) {
        this.responseEntity = responseEntity;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public GiftIntegralDto getGiftIntegralDto() {
        return giftIntegralDto;
    }

    public void setGiftIntegralDto(GiftIntegralDto giftIntegralDto) {
        this.giftIntegralDto = giftIntegralDto;
    }
}
