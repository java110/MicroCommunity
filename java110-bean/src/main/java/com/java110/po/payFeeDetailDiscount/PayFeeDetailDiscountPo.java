package com.java110.po.payFeeDetailDiscount;

import java.io.Serializable;

public class PayFeeDetailDiscountPo implements Serializable {

    private String detailDiscountId;
    private String discountPrice;
    private String detailId;
    private String remark;
    private String communityId;
    private String discountId;
    private String feeId;
    private String bId;
    private String statusCd="0";

    public String getDetailDiscountId() {
        return detailDiscountId;
    }

    public void setDetailDiscountId(String detailDiscountId) {
        this.detailDiscountId = detailDiscountId;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }
}
