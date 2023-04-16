package com.java110.dto.chargeMachine;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 充电插口数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ChargeMachinePortDto extends PageDto implements Serializable {

    public static final String STATE_FREE = "FREE";//设备插座工作状态，FREE 空闲，WORKING 忙碌，BREAKDOWN 故障
    public static final String STATE_WORKING = "WORKING";//设备插座工作状态，FREE 空闲，WORKING 忙碌，BREAKDOWN 故障
    public static final String STATE_BREAKDOWN = "BREAKDOWN";//设备插座工作状态，FREE 空闲，WORKING 忙碌，BREAKDOWN 故障

    private String machineId;
    private String portName;
    private String state;
    private String stateName;
    private String portId;
    private String portCode;
    private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
