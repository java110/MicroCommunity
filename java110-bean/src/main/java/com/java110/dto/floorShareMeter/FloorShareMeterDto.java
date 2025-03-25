package com.java110.dto.floorShareMeter;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 楼栋公摊表数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FloorShareMeterDto extends PageDto implements Serializable {

    private String floorId;
private String meterNum;
private String meterType;
private String fsmId;
private String formulaValue;
private String communityId;
private String curDegree;
private String shareType;
private String floorNum;
private String curReadingTime;


    private Date createTime;

    private String statusCd = "0";


    public String getFloorId() {
        return floorId;
    }
public void setFloorId(String floorId) {
        this.floorId = floorId;
    }
public String getMeterNum() {
        return meterNum;
    }
public void setMeterNum(String meterNum) {
        this.meterNum = meterNum;
    }
public String getMeterType() {
        return meterType;
    }
public void setMeterType(String meterType) {
        this.meterType = meterType;
    }
public String getFsmId() {
        return fsmId;
    }
public void setFsmId(String fsmId) {
        this.fsmId = fsmId;
    }
public String getFormulaValue() {
        return formulaValue;
    }
public void setFormulaValue(String formulaValue) {
        this.formulaValue = formulaValue;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getCurDegree() {
        return curDegree;
    }
public void setCurDegree(String curDegree) {
        this.curDegree = curDegree;
    }
public String getShareType() {
        return shareType;
    }
public void setShareType(String shareType) {
        this.shareType = shareType;
    }
public String getFloorNum() {
        return floorNum;
    }
public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }
public String getCurReadingTime() {
        return curReadingTime;
    }
public void setCurReadingTime(String curReadingTime) {
        this.curReadingTime = curReadingTime;
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
