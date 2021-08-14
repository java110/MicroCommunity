package com.java110.po.payFeeDetailMonth;

import java.io.Serializable;
import java.util.Date;

public class PayFeeDetailMonthPo implements Serializable {

    private String detailMonth;
private String detailYear;
private String detailId;
private String receivableAmount;
private String discountAmount;
private String remark;
private String statusCd = "0";
private String receivedAmount;
private String communityId;
private String feeId;
private String monthId;
public String getDetailMonth() {
        return detailMonth;
    }
public void setDetailMonth(String detailMonth) {
        this.detailMonth = detailMonth;
    }
public String getDetailYear() {
        return detailYear;
    }
public void setDetailYear(String detailYear) {
        this.detailYear = detailYear;
    }
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getReceivableAmount() {
        return receivableAmount;
    }
public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }
public String getDiscountAmount() {
        return discountAmount;
    }
public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getReceivedAmount() {
        return receivedAmount;
    }
public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getFeeId() {
        return feeId;
    }
public void setFeeId(String feeId) {
        this.feeId = feeId;
    }
public String getMonthId() {
        return monthId;
    }
public void setMonthId(String monthId) {
        this.monthId = monthId;
    }



}
