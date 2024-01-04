package com.java110.dto.fee;

import com.java110.po.fee.PayFeeDetailPo;

import java.io.Serializable;

public class PayFeeDataDto extends PayFeeDetailPo implements Serializable {

    public static final String TEMP_CYCLE_DEFAULT="-100";
    public static final String TEMP_CYCLE_CUSTOM_AMOUNT="-101"; //自定义金额交费
    public static final String TEMP_CYCLE_CUSTOM_CYCLE ="-102"; // 自定义周期
    public static final String TEMP_CYCLE_CUSTOM_END_TIME="-103"; //自定义结束时间
    public static final String TEMP_CYCLE_CUSTOM_START_END_TIME="-105"; //自定义缴费时间段

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
