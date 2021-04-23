package com.java110.po.feeDiscountSpec;

import java.io.Serializable;

public class FeeDiscountSpecPo implements Serializable {

    private String discountSpecId;
    private String specId;
    private String specName;
    private String specValue;
    private String statusCd = "0";
    private String discountId;
    private String communityId;

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecValue() {
        return specValue;
    }

    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getDiscountSpecId() {
        return discountSpecId;
    }

    public void setDiscountSpecId(String discountSpecId) {
        this.discountSpecId = discountSpecId;
    }
}
