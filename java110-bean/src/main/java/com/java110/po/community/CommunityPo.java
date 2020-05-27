package com.java110.po.community;

import java.io.Serializable;

/**
 * @ClassName CommunityPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/27 14:42
 * @Version 1.0
 * add by wuxw 2020/5/27
 **/
public class CommunityPo implements Serializable {

    private String communityId;
    private String name;
    private String address;
    private String nearbyLandmarks;
    private String cityCode;
    private String mapX;
    private String mapY;
    private String state;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
