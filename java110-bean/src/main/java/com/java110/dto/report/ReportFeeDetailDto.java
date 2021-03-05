package com.java110.dto.report;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @ClassName ReportRoomDto
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 22:20
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
public class ReportFeeDetailDto extends PageDto implements Serializable {
    private String detailId;
    private String feeId;
    private String communityId;
    private String cycles;
    private String receivableAmount;
    private String receivedAmount;
    private String primeRate;
    private String remark;
    private String startTime;
    private String endTime;
    private String createTime;
    private String state;
    private String curStartYear;
    private String curEndYear;
    private String configId;
    private String payerObjId;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCycles() {
        return cycles;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
    }

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getPrimeRate() {
        return primeRate;
    }

    public void setPrimeRate(String primeRate) {
        this.primeRate = primeRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCurStartYear() {
        return curStartYear;
    }

    public void setCurStartYear(String curStartYear) {
        this.curStartYear = curStartYear;
    }

    public String getCurEndYear() {
        return curEndYear;
    }

    public void setCurEndYear(String curEndYear) {
        this.curEndYear = curEndYear;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
    }
}
