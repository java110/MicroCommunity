package com.java110.dto.fee;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 临时车费用结果
 */
public class TempCarFeeResult implements Serializable {

    public TempCarFeeResult(String carNum, Double payCharge, Double maxMoney, Double baseMoney) {
        this.carNum = carNum;
        this.payCharge = payCharge;
        if (maxMoney < payCharge) {
            this.payCharge = maxMoney;
        }
        this.payCharge = new BigDecimal(this.payCharge).add(new BigDecimal(baseMoney)).doubleValue();
    }

    private String carNum;

    private Double payCharge;

    private long hours;

    private long min;

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public Double getPayCharge() {
        return payCharge;
    }

    public void setPayCharge(Double payCharge) {
        this.payCharge = payCharge;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }
}
