package com.java110.dto.parkingSpaceApply;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 车位申请数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingSpaceApplyDto extends PageDto implements Serializable {

    private String carBrand;
private String applyPersonName;
private String carNum;
private String psId;
private String remark;
private String applyId;
private String carColor;
private String carType;
private String configId;
private String applyPersonLink;
private String startTime;
private String applyPersonId;
private String endTime;
private String state;
private String communityId;
private String feeId;


    private Date createTime;

    private String statusCd = "0";


    public String getCarBrand() {
        return carBrand;
    }
public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }
public String getApplyPersonName() {
        return applyPersonName;
    }
public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
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
public String getApplyId() {
        return applyId;
    }
public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
public String getCarColor() {
        return carColor;
    }
public void setCarColor(String carColor) {
        this.carColor = carColor;
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
public String getApplyPersonLink() {
        return applyPersonLink;
    }
public void setApplyPersonLink(String applyPersonLink) {
        this.applyPersonLink = applyPersonLink;
    }
public String getStartTime() {
        return startTime;
    }
public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
public String getApplyPersonId() {
        return applyPersonId;
    }
public void setApplyPersonId(String applyPersonId) {
        this.applyPersonId = applyPersonId;
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

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }
}
