package com.java110.po.fee;

import java.io.Serializable;

/**
 * @ClassName PayFeeConfig
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/27 21:43
 * @Version 1.0
 * add by wuxw 2020/5/27
 **/
public class PayFeeConfigPo implements Serializable {

    private String configId;

    private String communityId;
    private String feeTypeCd;
    private String squarePrice;
    private String additionalAmount;

    private String isDefault;
    private String startTime;
    private String endTime;
    private String feeFlag;
    private String feeName;
    private String computingFormula;
    private String computingFormulaText;

    private String billType;

    private String paymentCd;

    private String paymentCycle;
    private String deductFrom;
    private String statusCd = "0";

    private String payOnline;
    private String scale;
    private String decimalPlace;
    private String units;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }

    public String getSquarePrice() {
        return squarePrice;
    }

    public void setSquarePrice(String squarePrice) {
        this.squarePrice = squarePrice;
    }

    public String getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(String additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
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

    public String getFeeFlag() {
        return feeFlag;
    }

    public void setFeeFlag(String feeFlag) {
        this.feeFlag = feeFlag;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getComputingFormula() {
        return computingFormula;
    }

    public void setComputingFormula(String computingFormula) {
        this.computingFormula = computingFormula;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getPaymentCd() {
        return paymentCd;
    }

    public void setPaymentCd(String paymentCd) {
        this.paymentCd = paymentCd;
    }

    public String getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(String paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public String getComputingFormulaText() {
        return computingFormulaText;
    }

    public void setComputingFormulaText(String computingFormulaText) {
        this.computingFormulaText = computingFormulaText;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getDeductFrom() { return deductFrom; }

    public void setDeductFrom(String deductFrom) { this.deductFrom = deductFrom; }

    public String getPayOnline() {
        return payOnline;
    }

    public void setPayOnline(String payOnline) {
        this.payOnline = payOnline;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getDecimalPlace() {
        return decimalPlace;
    }

    public void setDecimalPlace(String decimalPlace) {
        this.decimalPlace = decimalPlace;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
