package com.java110.dto.fee;

import com.java110.po.fee.PayFeeDetailPo;

import java.io.Serializable;

public class PayFeeDataDto extends PayFeeDetailPo implements Serializable {

    private String tempCycle;

    private String custEndTime;

    private FeeDto feeDto;

    private double accountAmount;

    private String acctId;

    private FeeConfigDto feeConfigDto;

    public String getTempCycle() {
        return tempCycle;
    }

    public void setTempCycle(String tempCycle) {
        this.tempCycle = tempCycle;
    }

    public String getCustEndTime() {
        return custEndTime;
    }

    public void setCustEndTime(String custEndTime) {
        this.custEndTime = custEndTime;
    }

    public FeeDto getFeeDto() {
        return feeDto;
    }

    public void setFeeDto(FeeDto feeDto) {
        this.feeDto = feeDto;
    }

    public FeeConfigDto getFeeConfigDto() {
        return feeConfigDto;
    }

    public void setFeeConfigDto(FeeConfigDto feeConfigDto) {
        this.feeConfigDto = feeConfigDto;
    }

    public double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(double accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }
}
