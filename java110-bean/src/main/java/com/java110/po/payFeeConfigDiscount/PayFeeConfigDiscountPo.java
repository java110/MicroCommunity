package com.java110.po.payFeeConfigDiscount;

import java.io.Serializable;
import java.util.Date;

public class PayFeeConfigDiscountPo implements Serializable {

    private String configDiscountId;
    private String configId;
    private String statusCd = "0";
    private String discountId;
    private String communityId;
    private String startTime;
    private String endTime;
    private String payMaxEndTime;

    public String getConfigDiscountId() {
        return configDiscountId;
    }

    public void setConfigDiscountId(String configDiscountId) {
        this.configDiscountId = configDiscountId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
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


    public String getPayMaxEndTime() {
        return payMaxEndTime;
    }

    public void setPayMaxEndTime(String payMaxEndTime) {
        this.payMaxEndTime = payMaxEndTime;
    }
}
