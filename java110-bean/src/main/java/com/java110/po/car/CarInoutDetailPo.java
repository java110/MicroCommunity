package com.java110.po.car;

import java.io.Serializable;

/**
 * @ClassName CarInout
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/27 14:31
 * @Version 1.0
 * add by wuxw 2020/5/27
 **/
public class CarInoutDetailPo implements Serializable {

    private String detailId;
    private String inoutId;
    private String communityId;
    private String machineId;
    private String machineCode;
    private String carInout;
    private String carNum;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getInoutId() {
        return inoutId;
    }

    public void setInoutId(String inoutId) {
        this.inoutId = inoutId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String getCarInout() {
        return carInout;
    }

    public void setCarInout(String carInout) {
        this.carInout = carInout;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }
}
