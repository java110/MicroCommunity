package com.java110.dto.tempCarFeeConfig;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

public class TempCarPayOrderDto extends PageDto implements Serializable {

    private String orderId;
    private String paId;
    private String extPaId;
    private String carNum;
    private double stopTimeTotal;
    private double payCharge;
    private Date inTime;
    private Date queryTime;
    private double amount;
    private Date payTime;
    private String payType;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public double getStopTimeTotal() {
        return stopTimeTotal;
    }

    public void setStopTimeTotal(double stopTimeTotal) {
        this.stopTimeTotal = stopTimeTotal;
    }

    public double getPayCharge() {
        return payCharge;
    }

    public void setPayCharge(double payCharge) {
        this.payCharge = payCharge;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public String getExtPaId() {
        return extPaId;
    }

    public void setExtPaId(String extPaId) {
        this.extPaId = extPaId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
