package com.java110.dto.couponPool;

import java.io.Serializable;

public class CouponQrCodeDto implements Serializable{


    public CouponQrCodeDto(String couponId, String qrCode, String remark) {
        this.couponId = couponId;
        this.qrCode = qrCode;
        this.remark = remark;
    }

    public CouponQrCodeDto() {

    }

    private String couponId;

    private String qrCode;

    private String remark;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
