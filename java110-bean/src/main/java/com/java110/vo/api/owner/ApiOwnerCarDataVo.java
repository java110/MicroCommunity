package com.java110.vo.api.owner;

import com.java110.vo.Vo;

import java.util.Date;

/**
 * @ClassName ApiFloorDataVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/4/24 11:18
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ApiOwnerCarDataVo extends Vo {
    private String carColor;
    private String carBrand;
    private String carType;
    private String carTypeName;
    private String carNum;
    private String psId;
    private String remark;
    private String ownerId;
    private String userId;
    private String carId;
    private String carTypeCd;
    private String memberId;



    private String createTime;

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCarTypeCd() {
        return carTypeCd;
    }

    public void setCarTypeCd(String carTypeCd) {
        this.carTypeCd = carTypeCd;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
