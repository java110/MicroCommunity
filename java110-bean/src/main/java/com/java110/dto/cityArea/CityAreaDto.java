package com.java110.dto.cityArea;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 地区数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CityAreaDto extends PageDto implements Serializable {

    private String areaCode;
    private String areaName;
    private String areaLevel;
    private String lon;
    private String id;
    private String parentAreaCode;
    private String parentAreaName;
    private String lat;


    private Date createTime;

    private String statusCd = "0";


    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentAreaCode() {
        return parentAreaCode;
    }

    public void setParentAreaCode(String parentAreaCode) {
        this.parentAreaCode = parentAreaCode;
    }

    public String getParentAreaName() {
        return parentAreaName;
    }

    public void setParentAreaName(String parentAreaName) {
        this.parentAreaName = parentAreaName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
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
