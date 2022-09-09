package com.java110.dto.machine;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 进出场详情数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CarInoutDetailDto extends CarInoutDto implements Serializable {

    public static final String CAR_INOUT_IN = "3306";
    public static final String CAR_INOUT_OUT = "3307";
    public static final String CAR_TYPE_TEMP = "1003";
    public static final String CAR_TYPE_MONTH = "1001";
    private String inoutId;
    private String machineId;
    private String machineCode;
    private String carInout;
    private String detailId;
    private String carNum;
    private String communityId;


    private Date createTime;

    private String statusCd = "0";

    private String remark;
    private String state;
    private String inState;

    private String carType;
    private String carTypeName;

    private String configId;

    private String photoJpg;

    public String getInoutId() {
        return inoutId;
    }

    public void setInoutId(String inoutId) {
        this.inoutId = inoutId;
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

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }


    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    @Override
    public String getCarTypeName() {
        return carTypeName;
    }

    @Override
    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getInState() {
        return inState;
    }

    public void setInState(String inState) {
        this.inState = inState;
    }

    public String getPhotoJpg() {
        return photoJpg;
    }

    public void setPhotoJpg(String photoJpg) {
        this.photoJpg = photoJpg;
    }
}
