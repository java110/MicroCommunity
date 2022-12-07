package com.java110.vo.api.fee;

import com.java110.dto.fee.FeeAttrDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ApiFeeDataVo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/2/1 15:54
 * @Version 1.0
 * add by wuxw 2020/2/1
 **/
public class ApiFeeDataVo implements Serializable {
    private String amount;
    private String incomeObjId;
    private String feeTypeCd;
    private String startTime;
    private String endTime;
    private String communityId;
    private String feeId;
    private String userId;
    private String payerObjId;
    private String payerObjName;
    private String payerObjType;
    private String configId;

    private String state;
    private String stateName;
    private String feeFlag;
    private String feeName;
    private String feeTypeCdName;
    private String feeFlagName;

    private String squarePrice;
    private String additionalAmount;

    private String feePrice;
    private String feeTotalPrice;
    private String isDefault;

    private String paymentCd;

    private String paymentCycle;
    private String computingFormula;
    private String deadlineTime;
    private String amountOwed;

    private String curDegrees;
    private String preDegrees;

    private String preReadingTime;
    private String curReadingTime;
    private String mwPrice;

    private String carTypeCd;

    private String batchId;

    //映射关系开关值
    private String val;

    private String offlinePayFeeSwitch;

    //实收金额映射开关值
    private String receivedAmountSwitch;

    private String deductFrom;

    private List<FeeAttrDto> feeAttrs;

    private String rateCycle;

    private String rate;

    private String rateStartTime;



    private String maxEndTime;

    private String payOnline;
    private String scale;
    private String decimalPlace;
    private String units;

    public String getMaxEndTime() {
        return maxEndTime;
    }

    public void setMaxEndTime(String maxEndTime) {
        this.maxEndTime = maxEndTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIncomeObjId() {
        return incomeObjId;
    }

    public void setIncomeObjId(String incomeObjId) {
        this.incomeObjId = incomeObjId;
    }

    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFeeFlag() {
        return feeFlag;
    }

    public void setFeeFlag(String feeFlag) {
        this.feeFlag = feeFlag;
    }

    public String getFeeTypeCdName() {
        return feeTypeCdName;
    }

    public void setFeeTypeCdName(String feeTypeCdName) {
        this.feeTypeCdName = feeTypeCdName;
    }

    public String getFeeFlagName() {
        return feeFlagName;
    }

    public void setFeeFlagName(String feeFlagName) {
        this.feeFlagName = feeFlagName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getFeePrice() {
        return feePrice;
    }

    public void setFeePrice(String feePrice) {
        this.feePrice = feePrice;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
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

    public String getComputingFormula() {
        return computingFormula;
    }

    public void setComputingFormula(String computingFormula) {
        this.computingFormula = computingFormula;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(String amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getPayerObjType() {
        return payerObjType;
    }

    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
    }

    public String getCurDegrees() {
        return curDegrees;
    }

    public void setCurDegrees(String curDegrees) {
        this.curDegrees = curDegrees;
    }

    public String getPreDegrees() {
        return preDegrees;
    }

    public void setPreDegrees(String preDegrees) {
        this.preDegrees = preDegrees;
    }

    public String getPreReadingTime() {
        return preReadingTime;
    }

    public void setPreReadingTime(String preReadingTime) {
        this.preReadingTime = preReadingTime;
    }

    public String getCurReadingTime() {
        return curReadingTime;
    }

    public void setCurReadingTime(String curReadingTime) {
        this.curReadingTime = curReadingTime;
    }

    public List<FeeAttrDto> getFeeAttrs() {
        return feeAttrs;
    }

    public void setFeeAttrs(List<FeeAttrDto> feeAttrs) {
        this.feeAttrs = feeAttrs;
    }

    public String getMwPrice() {
        return mwPrice;
    }

    public void setMwPrice(String mwPrice) {
        this.mwPrice = mwPrice;
    }

    public String getCarTypeCd() {
        return carTypeCd;
    }

    public void setCarTypeCd(String carTypeCd) {
        this.carTypeCd = carTypeCd;
    }


    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getFeeTotalPrice() {
        return feeTotalPrice;
    }

    public void setFeeTotalPrice(String feeTotalPrice) {
        this.feeTotalPrice = feeTotalPrice;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getOfflinePayFeeSwitch() {
        return offlinePayFeeSwitch;
    }

    public void setOfflinePayFeeSwitch(String offlinePayFeeSwitch) {
        this.offlinePayFeeSwitch = offlinePayFeeSwitch;
    }

    public String getReceivedAmountSwitch() {
        return receivedAmountSwitch;
    }

    public void setReceivedAmountSwitch(String receivedAmountSwitch) {
        this.receivedAmountSwitch = receivedAmountSwitch;
    }

    public String getPayerObjName() {
        return payerObjName;
    }

    public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
    }

    public String getDeductFrom() { return deductFrom; }

    public void setDeductFrom(String deductFrom) { this.deductFrom = deductFrom; }

    public String getRateCycle() {
        return rateCycle;
    }

    public void setRateCycle(String rateCycle) {
        this.rateCycle = rateCycle;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRateStartTime() {
        return rateStartTime;
    }

    public void setRateStartTime(String rateStartTime) {
        this.rateStartTime = rateStartTime;
    }

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
