package com.java110.po.fee;

import java.io.Serializable;

/**
 * @ClassName PayFeeDetailPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/27 23:10
 * @Version 1.0
 * add by wuxw 2020/5/27
 **/
public class PayFeeDetailPo implements Serializable {

    private String detailId;
    private String feeId;
    private String communityId;
    private String cycles;
    private String receivableAmount;
    private String receivedAmount;
    private String payableAmount;
    private String primeRate;
    private String remark;
    private String startTime;
    private String endTime;
    private String createTime;
    private String bId;
    private String statusCd = "0";

    private String state;

    private String payOrderId;

    /**
     * 收银员
     */
    private String cashierId;
    private String cashierName;

    private String openInvoice; // todo 默认 未开票


    private String acctAmount = "0";
    private String discountAmount = "0";
    private String deductionAmount = "0";
    private String lateAmount = "0";
    private String giftAmount = "0";


    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCycles() {
        return cycles;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
    }

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getPrimeRate() {
        return primeRate;
    }

    public void setPrimeRate(String primeRate) {
        this.primeRate = primeRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getOpenInvoice() {
        return openInvoice;
    }

    public void setOpenInvoice(String openInvoice) {
        this.openInvoice = openInvoice;
    }

    public String getAcctAmount() {
        return acctAmount;
    }

    public void setAcctAmount(String acctAmount) {
        this.acctAmount = acctAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(String deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public String getLateAmount() {
        return lateAmount;
    }

    public void setLateAmount(String lateAmount) {
        this.lateAmount = lateAmount;
    }

    public String getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(String giftAmount) {
        this.giftAmount = giftAmount;
    }
}
