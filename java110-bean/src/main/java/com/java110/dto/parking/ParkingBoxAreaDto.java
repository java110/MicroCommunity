package com.java110.dto.parking;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 停车场岗亭数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingBoxAreaDto extends PageDto implements Serializable {

    public static final String DEFAULT_AREA_TRUE = "T";
    public static final String DEFAULT_AREA_FALSE = "F";

    private String defaultArea;
    private String baId;
    private String paId;
    private String remark;
    private String communityId;
    private String boxId;
    private String paNum;


    private Date createTime;

    private String statusCd = "0";


    public String getDefaultArea() {
        return defaultArea;
    }

    public void setDefaultArea(String defaultArea) {
        this.defaultArea = defaultArea;
    }

    public String getBaId() {
        return baId;
    }

    public void setBaId(String baId) {
        this.baId = baId;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
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

    public String getPaNum() {
        return paNum;
    }

    public void setPaNum(String paNum) {
        this.paNum = paNum;
    }
}
