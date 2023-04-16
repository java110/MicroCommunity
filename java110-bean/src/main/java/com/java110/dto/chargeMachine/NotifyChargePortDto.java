package com.java110.dto.chargeMachine;

import java.io.Serializable;
import java.util.Date;

public class NotifyChargePortDto implements Serializable {

    private String machineCode;
    private String portCode;

    private String energy ;

    private String orderId;

    private Date powerTime;


    public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getPowerTime() {
        return powerTime;
    }

    public void setPowerTime(Date powerTime) {
        this.powerTime = powerTime;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
}
