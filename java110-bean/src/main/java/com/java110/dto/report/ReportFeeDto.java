package com.java110.dto.report;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ReportRoomDto
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 22:20
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
public class ReportFeeDto extends PageDto implements Serializable {
    private String amount;
    private String incomeObjId;
    private String incomeObjName;
    private String feeTypeCd;
    private Date startTime;
    private Date endTime;
    private String communityId;
    private String feeId;
    private String userId;
    private String payerObjId;
    private String payerObjName;
    private String configId;

    private String squarePrice;
    private String mwPrice;
    private String additionalAmount;
    private String additionalAmountText;
    private String state;
    private String stateName;
    private String feeFlag;

    private String feeName;
    private String feeTypeCdName;
    private String feeFlagName;

    private double feePrice;
    private String payerObjType;
    private String computingFormula;
    private String isDefault;
    private double oweFee; // 欠费金额
    private String billType;
    private String billTypeName;

    private String paymentCd;

    private String paymentCycle;

    private String importFeeName;

    private String amountOwed;

    private String roomName;

    private String ownerName;

    private String ownerTel;


    private Date createTime;

    /**
     * 费用项开始时间
     */
    private Date configStartTime;
    /**
     * 费用项结束时间
     */
    private Date configEndTime;

    private Date deadlineTime;

    private Date importFeeEndTime;


    private String curDegrees;
    private String preDegrees;

    private Date preReadingTime;
    private Date curReadingTime;

    private String roomId;
    private String roomNum;
    private String carId;
    private String carNum;
    private String storeId;
    private String storeName;
    private String storeTypeCd;

    private String roomSubType;
    private String roomRent;
    private String roomArea;

    private String payOnline;
    private String scale;
    private String decimalPlace;
    private String units;




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

    public String getIncomeObjName() {
        return incomeObjName;
    }

    public void setIncomeObjName(String incomeObjName) {
        this.incomeObjName = incomeObjName;
    }

    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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

    public String getPayerObjName() {
        return payerObjName;
    }

    public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
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

    public double getFeePrice() {
        return feePrice;
    }

    public void setFeePrice(double feePrice) {
        this.feePrice = feePrice;
    }

    public String getPayerObjType() {
        return payerObjType;
    }

    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
    }

    public String getComputingFormula() {
        return computingFormula;
    }

    public void setComputingFormula(String computingFormula) {
        this.computingFormula = computingFormula;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public double getOweFee() {
        return oweFee;
    }

    public void setOweFee(double oweFee) {
        this.oweFee = oweFee;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillTypeName() {
        return billTypeName;
    }

    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
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

    public String getImportFeeName() {
        return importFeeName;
    }

    public void setImportFeeName(String importFeeName) {
        this.importFeeName = importFeeName;
    }

    public String getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(String amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getConfigStartTime() {
        return configStartTime;
    }

    public void setConfigStartTime(Date configStartTime) {
        this.configStartTime = configStartTime;
    }

    public Date getConfigEndTime() {
        return configEndTime;
    }

    public void setConfigEndTime(Date configEndTime) {
        this.configEndTime = configEndTime;
    }

    public Date getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(Date deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public Date getImportFeeEndTime() {
        return importFeeEndTime;
    }

    public void setImportFeeEndTime(Date importFeeEndTime) {
        this.importFeeEndTime = importFeeEndTime;
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

    public Date getPreReadingTime() {
        return preReadingTime;
    }

    public void setPreReadingTime(Date preReadingTime) {
        this.preReadingTime = preReadingTime;
    }

    public Date getCurReadingTime() {
        return curReadingTime;
    }

    public void setCurReadingTime(Date curReadingTime) {
        this.curReadingTime = curReadingTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreTypeCd() {
        return storeTypeCd;
    }

    public void setStoreTypeCd(String storeTypeCd) {
        this.storeTypeCd = storeTypeCd;
    }

    public String getAdditionalAmountText() {
        return additionalAmountText;
    }

    public void setAdditionalAmountText(String additionalAmountText) {
        this.additionalAmountText = additionalAmountText;
    }

    public String getMwPrice() {
        return mwPrice;
    }

    public void setMwPrice(String mwPrice) {
        this.mwPrice = mwPrice;
    }

    public String getRoomSubType() {
        return roomSubType;
    }

    public void setRoomSubType(String roomSubType) {
        this.roomSubType = roomSubType;
    }

    public String getRoomRent() {
        return roomRent;
    }

    public void setRoomRent(String roomRent) {
        this.roomRent = roomRent;
    }

    public String getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(String roomArea) {
        this.roomArea = roomArea;
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
