package com.java110.dto.fee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 费用数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeDto extends PageDto implements Serializable {

    public static final String STATE_FINISH = "2009001"; // 收费结束
    public static final String STATE_DOING = "2008001"; // 收费中

    public static final String PAYER_OBJ_TYPE_ROOM = "3333"; //房屋 6666 是车位
    public static final String PAYER_OBJ_TYPE_PARKING_SPACE = "6666";//是车位
    public static final String PAYER_OBJ_TYPE_CONTRACT = "7777";//是合同
    public static final String PAYER_OBJ_TYPE_CAR = "6666";//是车位
    public static final String PAYER_OBJ_TYPE_RENTING = "9999";//房源ID

    public static final String FEE_FLAG_ONCE = "2006012";//一次性费用
    public static final String FEE_FLAG_CYCLE = "1003006";//周期性费用
    public static final String FEE_FLAG_CYCLE_ONCE = "4012024";//间接性费用
    public static final String REDIS_PAY_OWE_FEE = "PAY_OWE_FEE_";
    public static final String REDIS_PAY_TEMP_CAR_FEE = "PAY_TEMP_CAR_FEE_";
    public static final String REDIS_PAY_TEMP_CAR_FEE_COMMUNITY = "REDIS_PAY_TEMP_CAR_FEE_COMMUNITY_";

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
    private String[] payerObjIds;
    private String[] feeTypeCds;
    private String configId;

    //映射关系开关值
    private String val;

    //实收金额映射开关值
    private String receivedAmountSwitch;

    private String squarePrice;
    private String mwPrice;
    private String additionalAmount;
    private String state;
    private String stateName;
    private String feeFlag;

    private String feeName;
    private String feeTypeCdName;
    private String feeFlagName;

    private Date arrearsEndTime;
    private Date noArrearsEndTime;
    private double feePrice;
    private String payerObjType;
    private String computingFormula;
    private String computingFormulaText;
    private String isDefault;
    private double oweFee; // 欠费金额
    private String billType;
    private String billTypeName;
    private String builtUpArea;

    private String paymentCd;

    private String paymentCycle;

    private String bId;
    private String importFeeName;

    private String amountOwed;

    private String roomName;

    private String ownerName;

    private String ownerTel;

    private String ownerId;


    private Date createTime;


    private String statusCd = "0";

    /**
     * 费用项开始时间
     */
    private Date configStartTime;
    /**
     * 费用项结束时间
     */
    private Date configEndTime;

    private Date deadlineTime;

    private Date maxEndTime;

    private Date importFeeEndTime;


    private String curDegrees;
    private String preDegrees;

    private Date preReadingTime;
    private Date curReadingTime;

    private List<FeeAttrDto> feeAttrDtos;

    //当前时间
    private Date nowDate;

    //查询出的数量
    private int count;

    private String carTypeCd;
    private String cycle;
    private double feeTotalPrice;
    private String batchId;
    private String custEndTime;

    private String offlinePayFeeSwitch;

    private String deductFrom;

    private String rateCycle;

    private String rate;

    private String rateStartTime;

    private String payOnline;
    private String scale;
    private String decimalPlace;
    private String units;

    private String operate;

    private String userName;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
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

    public Date getArrearsEndTime() {
        return arrearsEndTime;
    }

    public void setArrearsEndTime(Date arrearsEndTime) {
        this.arrearsEndTime = arrearsEndTime;
    }

    public String[] getFeeTypeCds() {
        return feeTypeCds;
    }

    public void setFeeTypeCds(String[] feeTypeCds) {
        this.feeTypeCds = feeTypeCds;
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

    public String getFeeFlag() {
        return feeFlag;
    }

    public void setFeeFlag(String feeFlag) {
        this.feeFlag = feeFlag;
    }

    public String[] getPayerObjIds() {
        return payerObjIds;
    }

    public void setPayerObjIds(String[] payerObjIds) {
        this.payerObjIds = payerObjIds;
    }

    public Date getNoArrearsEndTime() {
        return noArrearsEndTime;
    }

    public void setNoArrearsEndTime(Date noArrearsEndTime) {
        this.noArrearsEndTime = noArrearsEndTime;
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

    public double getOweFee() {
        return oweFee;
    }

    public void setOweFee(double oweFee) {
        this.oweFee = oweFee;
    }

    public String getPayerObjName() {
        return payerObjName;
    }

    public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
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

    public List<FeeAttrDto> getFeeAttrDtos() {
        return feeAttrDtos;
    }

    public void setFeeAttrDtos(List<FeeAttrDto> feeAttrDtos) {
        this.feeAttrDtos = feeAttrDtos;
    }

    public String getIncomeObjName() {
        return incomeObjName;
    }

    public void setIncomeObjName(String incomeObjName) {
        this.incomeObjName = incomeObjName;
    }

    public String getBuiltUpArea() {
        return builtUpArea;
    }

    public void setBuiltUpArea(String builtUpArea) {
        this.builtUpArea = builtUpArea;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String value) {
        this.val = value;
    }

    public String getReceivedAmountSwitch() {
        return receivedAmountSwitch;
    }

    public void setReceivedAmountSwitch(String receivedAmountSwitch) {
        this.receivedAmountSwitch = receivedAmountSwitch;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getNowDate() {
        return nowDate;
    }

    public void setNowDate(Date nowDate) {
        this.nowDate = nowDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getComputingFormulaText() {
        return computingFormulaText;
    }

    public void setComputingFormulaText(String computingFormulaText) {
        this.computingFormulaText = computingFormulaText;
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

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public double getFeeTotalPrice() {
        return feeTotalPrice;
    }

    public void setFeeTotalPrice(double feeTotalPrice) {
        this.feeTotalPrice = feeTotalPrice;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getOfflinePayFeeSwitch() {
        return offlinePayFeeSwitch;
    }

    public void setOfflinePayFeeSwitch(String offlinePayFeeSwitch) {
        this.offlinePayFeeSwitch = offlinePayFeeSwitch;
    }

    public String getDeductFrom() { return deductFrom; }

    public void setDeductFrom(String deductFrom) { this.deductFrom = deductFrom; }
    public String getCustEndTime() {
        return custEndTime;
    }

    public void setCustEndTime(String custEndTime) {
        this.custEndTime = custEndTime;
    }

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

    public Date getMaxEndTime() {
        return maxEndTime;
    }

    public void setMaxEndTime(Date maxEndTime) {
        this.maxEndTime = maxEndTime;
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

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
