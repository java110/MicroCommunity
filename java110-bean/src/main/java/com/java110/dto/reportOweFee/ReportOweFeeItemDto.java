package com.java110.dto.reportOweFee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 欠费统计数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportOweFeeItemDto extends PageDto implements Serializable {

    private String configId;
    private String amountOwed;
    private String payerObjName;
    private String feeName;
    private String configName;
    private String payerObjId;
    private Date startTime;
    private Date endTime;
    private double totalOweAmount;


    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(String amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getPayerObjName() {
        return payerObjName;
    }

    public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
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

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public double getTotalOweAmount() {
        return totalOweAmount;
    }

    public void setTotalOweAmount(double totalOweAmount) {
        this.totalOweAmount = totalOweAmount;
    }
}
