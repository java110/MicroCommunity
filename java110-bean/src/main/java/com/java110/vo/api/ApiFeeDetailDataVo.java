package com.java110.vo.api;

import com.java110.dto.fee.FeeAccountDetailDto;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;
import com.java110.vo.Vo;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ApiFeeVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/6/2 18:05
 * @Version 1.0
 * add by wuxw 2019/6/2
 **/
public class ApiFeeDetailDataVo extends Vo {
    private String primeRate;
    private String detailId;
    private String receivableAmount;
    private String cycles;
    private String remark;
    private String receivedAmount;
    private String communityId;
    private String feeId;

    private String createTime;
    private String state;
    private String stateName;
    private String startTime;
    private String endTime;
    private String feeName;
    private String curDegrees;
    private String preDegrees;

    private Date preReadingTime;
    private Date curReadingTime;

    private String cashierId;
    private String cashierName;

    private List<FeeAccountDetailDto> feeAccountDetailDtoList;
    private List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtoList;

    private String fadState;
    private String fadStateName;
    private String amount;

    private String primeRateName;

    private String payerObjName;


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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
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
