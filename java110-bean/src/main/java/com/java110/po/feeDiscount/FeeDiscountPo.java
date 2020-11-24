package com.java110.po.feeDiscount;

import java.io.Serializable;
import java.util.Date;

public class FeeDiscountPo implements Serializable {

    private String discountName;
private String discountDesc;
private String discountType;
private String statusCd = "0";
private String discountId;
private String communityId;
private String ruleId;
public String getDiscountName() {
        return discountName;
    }
public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }
public String getDiscountDesc() {
        return discountDesc;
    }
public void setDiscountDesc(String discountDesc) {
        this.discountDesc = discountDesc;
    }
public String getDiscountType() {
        return discountType;
    }
public void setDiscountType(String discountType) {
        this.discountType = discountType;
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
public String getRuleId() {
        return ruleId;
    }
public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }



}
