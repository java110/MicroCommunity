package com.java110.dto.report;

import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeConfigDto;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ReportDeposit
 * @Description TODO
 * @Author fqz
 * @Date 2021/07/295 16:21
 **/
public class ReportDeposit extends PageDto implements Serializable {

    private String amount;

    private String incomeObjId;

    private String feeTypeCd;

    private String startTime;

    private String endTime;

    private String statusCd = "0";

    private String communityId;

    private String bId;

    private String feeId;

    private String userId;

    private String payerObjId;

    private String squarePrice;

    private String additionalAmount;

    private String feeFlag;

    private String state;

    private String configId;

    private String feeName;

    private String feeTypeCdName;

    private String stateName;

    private String feeFlagName;

    private String computingFormula;

    private String computingFormulaText;

    private String billType;

    private String payerObjType;

    private String isDefault;

    private String configStartTime;

    private String configEndTime;

    private String paymentCd;

    private String paymentCycle;

    private String billTypeName;

    private String createTime;

    private String payerObjTypeName;

    private List<FeeConfigDto> feeConfigDtos;

    private String feeStartTime;

    private String feeEndTime;

    private String detailState;

    private String detailStateName;

    private String deadlineTime;
    private String objName;
    private String ownerName;
    private String floorNum;
    private String unitNum;
    private String roomNum;
    private String carNum;
    private String floorId;
    private String unitId;

    private String allAmount;

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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
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

    public String getFeeFlag() {
        return feeFlag;
    }

    public void setFeeFlag(String feeFlag) {
        this.feeFlag = feeFlag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getFeeTypeCdName() {
        return feeTypeCdName;
    }

    public void setFeeTypeCdName(String feeTypeCdName) {
        this.feeTypeCdName = feeTypeCdName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getFeeFlagName() {
        return feeFlagName;
    }

    public void setFeeFlagName(String feeFlagName) {
        this.feeFlagName = feeFlagName;
    }

    public String getComputingFormula() {
        return computingFormula;
    }

    public void setComputingFormula(String computingFormula) {
        this.computingFormula = computingFormula;
    }

    public String getComputingFormulaText() {
        return computingFormulaText;
    }

    public void setComputingFormulaText(String computingFormulaText) {
        this.computingFormulaText = computingFormulaText;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getPayerObjType() {
        return payerObjType;
    }

    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getConfigStartTime() {
        return configStartTime;
    }

    public void setConfigStartTime(String configStartTime) {
        this.configStartTime = configStartTime;
    }

    public String getConfigEndTime() {
        return configEndTime;
    }

    public void setConfigEndTime(String configEndTime) {
        this.configEndTime = configEndTime;
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

    public String getBillTypeName() {
        return billTypeName;
    }

    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayerObjTypeName() {
        return payerObjTypeName;
    }

    public void setPayerObjTypeName(String payerObjTypeName) {
        this.payerObjTypeName = payerObjTypeName;
    }

    public List<FeeConfigDto> getFeeConfigDtos() {
        return feeConfigDtos;
    }

    public void setFeeConfigDtos(List<FeeConfigDto> feeConfigDtos) {
        this.feeConfigDtos = feeConfigDtos;
    }

    public String getFeeStartTime() {
        return feeStartTime;
    }

    public void setFeeStartTime(String feeStartTime) {
        this.feeStartTime = feeStartTime;
    }

    public String getFeeEndTime() {
        return feeEndTime;
    }

    public void setFeeEndTime(String feeEndTime) {
        this.feeEndTime = feeEndTime;
    }

    public String getDetailState() {
        return detailState;
    }

    public void setDetailState(String detailState) {
        this.detailState = detailState;
    }

    public String getDetailStateName() {
        return detailStateName;
    }

    public void setDetailStateName(String detailStateName) {
        this.detailStateName = detailStateName;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(String allAmount) {
        this.allAmount = allAmount;
    }
}
