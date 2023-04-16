package com.java110.dto.chargeMachine;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 充电扣款数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ChargeMachineOrderAcctDto extends PageDto implements Serializable {

    private String amount;
    private String cmoaId;
    private String orderId;
    private String acctDetailId;
    private String acctId;
    private String startTime;
    private String remark;
    private String endTime;
    private String communityId;
    private String energy;

    private String powerTime;


    private Date createTime;

    private String statusCd = "0";

    private String durationPrice;


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCmoaId() {
        return cmoaId;
    }

    public void setCmoaId(String cmoaId) {
        this.cmoaId = cmoaId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAcctDetailId() {
        return acctDetailId;
    }

    public void setAcctDetailId(String acctDetailId) {
        this.acctDetailId = acctDetailId;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
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

    public String getPowerTime() {
        return powerTime;
    }

    public void setPowerTime(String powerTime) {
        this.powerTime = powerTime;
    }

    public String getDurationPrice() {
        return durationPrice;
    }

    public void setDurationPrice(String durationPrice) {
        this.durationPrice = durationPrice;
    }
}
