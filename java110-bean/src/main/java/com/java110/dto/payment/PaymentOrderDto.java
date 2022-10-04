package com.java110.dto.payment;

import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * 支付订单信息
 * 封装实体类
 */
public class PaymentOrderDto implements Serializable{

    private String orderId;

    //金额
    private double money;

    private String name;


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
}
