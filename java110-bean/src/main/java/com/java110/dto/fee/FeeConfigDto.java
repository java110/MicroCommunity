package com.java110.dto.fee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 费用配置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeConfigDto extends PageDto implements Serializable {

    public static final String BILL_TYPE_YEAR = "001";// 按年出账
    public static final String BILL_TYPE_MONTH = "002";// 每月1日
    public static final String BILL_TYPE_DAY = "003";// 每日
    public static final String BILL_TYPE_EVERY = "004";// 实时
    public static final String BILL_TYPE_ONCE_MONTH = "005";// 一次性按月

    public static final String DEFAULT_FEE_CONFIG = "T";
    public static final String CUSTOME_FEE_CONFIG = "F";

    public static final String FEE_TYPE_CD_WATER = "888800010016";//电费
    public static final String FEE_TYPE_CD_METER = "888800010015";//水费
    public static final String FEE_TYPE_CD_GAS = "888800010009";//煤气
    public static final String FEE_TYPE_CD_SYSTEM = "888800010000";//系统
    public static final String FEE_TYPE_CD_PARKING = "888800010008";//系统
    public static final String FEE_TYPE_CD_RENT = "888800010018";// 租金
    public static final String CONFIG_ID_RENTING = "920000000000000000";//租赁费用项
    public static final String CONFIG_ID_GOODS = "930000000000000000";//商品费用项

    public static final String COMPUTING_FORMULA_TEMP_CAR = "9999";
    public static final String COMPUTING_FORMULA_RANT_RATE = "1102"; // 租金递增
    public static final String COMPUTING_FORMULA_DYNAMIC = "4004"; // 租金递增

    public static final String PAYMENT_CD_PRE = "1200";
    public static final String PAYMENT_CD_AFTER = "2100";

    public static final String DEDUCT_FROM_N = "N";


    private String feeTypeCd;
    private String computingFormula;
    private String computingFormulaName;
    private String additionalAmount;
    private String squarePrice;
    private String isDefault;
    private String configId;
    private String[] configIds;
    private String feeFlag;
    private String feeName;
    private String feeNameEq;
    private String startTime;
    private String endTime;
    private String curTime;
    private String communityId;
    private String feeTypeCdName;
    private String feeFlagName;

    private String billType;

    private String billTypeName;

    private String paymentCd;

    private String paymentCycle;

    private String valid;


    private Date createTime;

    private String statusCd = "0";
    private String computingFormulaText;

    private String deductFrom;

    private double amount;

    private String payOnline;
    private String scale;
    private String decimalPlace;
    private String units;


    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }

    public String getComputingFormula() {
        return computingFormula;
    }

    public void setComputingFormula(String computingFormula) {
        this.computingFormula = computingFormula;
    }

    public String getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(String additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public String getSquarePrice() {
        return squarePrice;
    }

    public void setSquarePrice(String squarePrice) {
        this.squarePrice = squarePrice;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
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

    public String getCurTime() {
        return curTime;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getComputingFormulaText() {
        return computingFormulaText;
    }

    public void setComputingFormulaText(String computingFormulaText) {
        this.computingFormulaText = computingFormulaText;
    }

    public String[] getConfigIds() {
        return configIds;
    }

    public void setConfigIds(String[] configIds) {
        this.configIds = configIds;
    }

    public String getFeeNameEq() {
        return feeNameEq;
    }

    public void setFeeNameEq(String feeNameEq) {
        this.feeNameEq = feeNameEq;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDeductFrom() {
        return deductFrom;
    }

    public void setDeductFrom(String deductFrom) {
        this.deductFrom = deductFrom;
    }

    public String getComputingFormulaName() {
        return computingFormulaName;
    }

    public void setComputingFormulaName(String computingFormulaName) {
        this.computingFormulaName = computingFormulaName;
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
