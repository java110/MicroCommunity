package com.java110.dto.meterWater;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 水电费数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ImportExportMeterWaterDto extends PageDto implements Serializable {

    private String remark;
    private String curReadingTime;
    private String curDegrees;
    private String meterType;
    private String preDegrees;
    private String preReadingTime;
    private String communityId;
    private String objType;
    private String floorNum;
    private String unitNum;
    private String roomNum;
    private double price;


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCurReadingTime() {
        return curReadingTime;
    }

    public void setCurReadingTime(String curReadingTime) {
        this.curReadingTime = curReadingTime;
    }

    public String getCurDegrees() {
        return curDegrees;
    }

    public void setCurDegrees(String curDegrees) {
        this.curDegrees = curDegrees;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getPreDegrees() {
        return preDegrees;
    }

    public void setPreDegrees(String preDegrees) {
        this.preDegrees = preDegrees;
    }

    public String getPreReadingTime() {
        return preReadingTime;
    }

    public void setPreReadingTime(String preReadingTime) {
        this.preReadingTime = preReadingTime;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
