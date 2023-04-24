package com.java110.dto.parking;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 岗亭数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingBoxDto extends PageDto implements Serializable {

    private String yelowCarIn;
    private String fee;
    private String tempCarIn;
    private String boxName;
    private String remark;
    private String communityId;
    private String blueCarIn;
    private String boxId;
    private String paId;
    private String paNum;


    private Date createTime;

    private String statusCd = "0";


    public String getYelowCarIn() {
        return yelowCarIn;
    }

    public void setYelowCarIn(String yelowCarIn) {
        this.yelowCarIn = yelowCarIn;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTempCarIn() {
        return tempCarIn;
    }

    public void setTempCarIn(String tempCarIn) {
        this.tempCarIn = tempCarIn;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getBlueCarIn() {
        return blueCarIn;
    }

    public void setBlueCarIn(String blueCarIn) {
        this.blueCarIn = blueCarIn;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
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

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getPaNum() {
        return paNum;
    }

    public void setPaNum(String paNum) {
        this.paNum = paNum;
    }
}
