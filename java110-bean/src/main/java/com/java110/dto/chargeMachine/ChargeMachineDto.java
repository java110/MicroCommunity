package com.java110.dto.chargeMachine;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 充电桩数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ChargeMachineDto extends PageDto implements Serializable {

    private String heartbeatTime;
private String implBean;
private String machineId;
private String machineCode;
private String energyPrice;
private String durationPrice;
private String communityId;
private String machineName;


    private Date createTime;

    private String statusCd = "0";


    public String getHeartbeatTime() {
        return heartbeatTime;
    }
public void setHeartbeatTime(String heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }
public String getImplBean() {
        return implBean;
    }
public void setImplBean(String implBean) {
        this.implBean = implBean;
    }
public String getMachineId() {
        return machineId;
    }
public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
public String getMachineCode() {
        return machineCode;
    }
public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
public String getEnergyPrice() {
        return energyPrice;
    }
public void setEnergyPrice(String energyPrice) {
        this.energyPrice = energyPrice;
    }
public String getDurationPrice() {
        return durationPrice;
    }
public void setDurationPrice(String durationPrice) {
        this.durationPrice = durationPrice;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getMachineName() {
        return machineName;
    }
public void setMachineName(String machineName) {
        this.machineName = machineName;
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
}
