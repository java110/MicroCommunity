package com.java110.po.store;

import java.io.Serializable;

/**
 * @ClassName StorePo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 22:03
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class StorePo implements Serializable {

    private String storeId;
    private String userId;
    private String name;
    private String address;
    private String tel;
    private String storeTypeCd;
    private String nearbyLandmarks;
    private String mapX;
    private String mapY;
    private String state;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStoreTypeCd() {
        return storeTypeCd;
    }

    public void setStoreTypeCd(String storeTypeCd) {
        this.storeTypeCd = storeTypeCd;
    }

    public String getNearbyLandmarks() {
        return nearbyLandmarks;
    }

    public void setNearbyLandmarks(String nearbyLandmarks) {
        this.nearbyLandmarks = nearbyLandmarks;
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
