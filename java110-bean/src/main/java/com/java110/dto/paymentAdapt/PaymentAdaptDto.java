package com.java110.dto.paymentAdapt;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 支付厂家数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PaymentAdaptDto extends PageDto implements Serializable {

    private String adaptId;
    private String beanJsapi;
    private String beanQrcode;
    private String beanNative;
    private String name;
    private String paymentType;

    private String beanRefund;


    private Date createTime;

    private String statusCd = "0";


    public String getAdaptId() {
        return adaptId;
    }

    public void setAdaptId(String adaptId) {
        this.adaptId = adaptId;
    }

    public String getBeanJsapi() {
        return beanJsapi;
    }

    public void setBeanJsapi(String beanJsapi) {
        this.beanJsapi = beanJsapi;
    }

    public String getBeanQrcode() {
        return beanQrcode;
    }

    public void setBeanQrcode(String beanQrcode) {
        this.beanQrcode = beanQrcode;
    }

    public String getBeanNative() {
        return beanNative;
    }

    public void setBeanNative(String beanNative) {
        this.beanNative = beanNative;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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

    public String getBeanRefund() {
        return beanRefund;
    }

    public void setBeanRefund(String beanRefund) {
        this.beanRefund = beanRefund;
    }
}
