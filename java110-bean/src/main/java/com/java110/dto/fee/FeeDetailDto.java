package com.java110.dto.fee;

import com.java110.dto.PageDto;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 费用明细数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeDetailDto extends PageDto implements Serializable {


    public static final String PRIME_REATE_WECHAT_QRCODE = "3";
    public static final String PRIME_REATE_ALI_QRCODE = "4";
    public static final String PRIME_REATE_WECHAT = "5";
    public static final String PRIME_REATE_CRASH = "1";
    public static final String PRIME_REATE_WECHAT_APP = "6";
    public static final String STATE_RETURNING = "1000";
    public static final String STATE_RETURNED = "1100";
    public static final String STATE_RETURN_ERROR = "1200";
    public static final String STATE_RETURN_ORDER = "1300";
    public static final String STATE_NORMAL = "1400";

    /**
     * 1000	退费中
     * 1100	已退费
     * 1200	退费失败
     * 1300	退费单
     * 1400	正常
     */

    private String primeRate;
    private String detailId;
    private String receivableAmount;
    private String cycles;
    private String remark;
    private String receivedAmount;
    private String payableAmount;
    private String communityId;
    private String feeId;
    private String bId;
    private String payerObjId;
    private String payerObjName;
    private String payerObjType;

    private Date createTime;
    private Date startTime;
    private Date endTime;

    private String statusCd = "0";
    private String state;
    private String stateName;
    private String curYear;
    private String configId;
    private String feeName;
    private String importFeeName;
    private String ownerId;
    private String curDegrees;
    private String preDegrees;

    private Date preReadingTime;
    private Date curReadingTime;
    private String ardId;
    private String[] states;

    private String payOrderId;

    private String cashierId;
    private String cashierName;

    private List<FeeAccountDetailDto> feeAccountDetailDtoList;
    private List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtoList;

    private String fadState;
    private String fadStateName;
    private String amount;

    private String primeRateName;

    public String getPrimeRate() {
        return primeRate;
    }

    public void setPrimeRate(String primeRate) {
        this.primeRate = primeRate;
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

    public String getCycles() {
        return cycles;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
    }

    public String getPayerObjType() {
        return payerObjType;
    }

    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
    }

    public String getCurYear() {
        return curYear;
    }

    public void setCurYear(String curYear) {
        this.curYear = curYear;
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

    public String getImportFeeName() {
        return importFeeName;
    }

    public void setImportFeeName(String importFeeName) {
        this.importFeeName = importFeeName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public String getArdId() {
        return ardId;
    }

    public void setArdId(String ardId) {
        this.ardId = ardId;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public String getFadState() {
        return fadState;
    }

    public void setFadState(String fadState) {
        this.fadState = fadState;
    }

    public String getFadStateName() {
        return fadStateName;
    }

    public void setFadStateName(String fadStateName) {
        this.fadStateName = fadStateName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<FeeAccountDetailDto> getFeeAccountDetailDtoList() {
        return feeAccountDetailDtoList;
    }

    public void setFeeAccountDetailDtoList(List<FeeAccountDetailDto> feeAccountDetailDtoList) {
        this.feeAccountDetailDtoList = feeAccountDetailDtoList;
    }

    public List<PayFeeDetailDiscountDto> getPayFeeDetailDiscountDtoList() {
        return payFeeDetailDiscountDtoList;
    }

    public void setPayFeeDetailDiscountDtoList(List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtoList) {
        this.payFeeDetailDiscountDtoList = payFeeDetailDiscountDtoList;
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

    public String getPrimeRateName() {
        return primeRateName;
    }

    public void setPrimeRateName(String primeRateName) {
        this.primeRateName = primeRateName;
    }

    public String getPayerObjName() {
        return payerObjName;
    }

    public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
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
}
