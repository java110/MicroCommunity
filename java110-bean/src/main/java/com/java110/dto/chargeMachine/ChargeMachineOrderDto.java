package com.java110.dto.chargeMachine;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 充电桩订单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ChargeMachineOrderDto extends PageDto implements Serializable {

    public static final String STATE_DOING = "1001";//1001 充电中  2002 充电完成 3003 充电失败
    public static final String STATE_FINISH = "2002";//1001 充电中  2002 充电完成 3003 充电失败
    public static final String STATE_FAIL = "3003";//1001 充电中  2002 充电完成 3003 充电失败
    public static final String STATE_FINISHING = "4004";//1001 充电中  2002 充电完成 3003 充电失败 4004 结束中

    private String amount;
    private String orderId;
    private String remark;
    private String portId;
    private String personName;
    private String machineId;
    private String acctDetailId;
    private String personId;
    private String chargeHours;
    private String startTime;
    private String endTime;

    private String queryTime;

    private String state;
    private String personTel;
    private String communityId;
    private String energy;

    private String machineCode;
    private String machineName;
    private String portCode;
    private String portName;

    private String durationPrice;

    private String stateName;


    private Date createTime;

    private String statusCd = "0";


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getAcctDetailId() {
        return acctDetailId;
    }

    public void setAcctDetailId(String acctDetailId) {
        this.acctDetailId = acctDetailId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getChargeHours() {
        return chargeHours;
    }

    public void setChargeHours(String chargeHours) {
        this.chargeHours = chargeHours;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPersonTel() {
        return personTel;
    }

    public void setPersonTel(String personTel) {
        this.personTel = personTel;
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

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getDurationPrice() {
        return durationPrice;
    }

    public void setDurationPrice(String durationPrice) {
        this.durationPrice = durationPrice;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }
}
