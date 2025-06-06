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

    public static final String SHARE_TYPE_ROOM_AREA = "1001";//1001 按面积 2002 按户 3003 自定义公式
    public static final String SHARE_TYPE_ROOM_COUNT = "2002";//1001 按面积 2002 按户 3003 自定义公式

    private String floorId;
    private String meterNum;
    private String meterType;
    private String meterTypeName;
    private String fsmId;
    private String formulaValue;
    private String communityId;
    private String curDegree;
    private String shareType;
    private String shareTypeName;
    private String floorNum;
    private String curReadingTime;

    private String configId;
    private String configName;

    private String sharePrice;


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

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public String getShareTypeName() {
        return shareTypeName;
    }

    public void setShareTypeName(String shareTypeName) {
        this.shareTypeName = shareTypeName;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(String sharePrice) {
        this.sharePrice = sharePrice;
    }
}
