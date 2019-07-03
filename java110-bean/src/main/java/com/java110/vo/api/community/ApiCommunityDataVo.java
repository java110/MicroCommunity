package com.java110.vo.api.community;

import java.io.Serializable;
import java.util.Date;

public class ApiCommunityDataVo implements Serializable {

    private String name;
private String address;
private String nearbyLandmarks;
private String cityCode;
private String mapX;
private String mapY;
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getAddress() {
        return address;
    }
public void setAddress(String address) {
        this.address = address;
    }
public String getNearbyLandmarks() {
        return nearbyLandmarks;
    }
public void setNearbyLandmarks(String nearbyLandmarks) {
        this.nearbyLandmarks = nearbyLandmarks;
    }
public String getCityCode() {
        return cityCode;
    }
public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
public String getMapX() {
        return mapX;
    }
public void setMapX(String mapX) {
        this.mapX = mapX;
    }
public String getMapY() {
        return mapY;
    }
public void setMapY(String mapY) {
        this.mapY = mapY;
    }



}
