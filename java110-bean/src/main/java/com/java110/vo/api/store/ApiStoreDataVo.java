package com.java110.vo.api.store;

import com.java110.dto.store.StoreAttrDto;
import com.java110.dto.store.StoreDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ApiStoreDataVo implements Serializable {
    private String storeId;
    private String userId;
    private String name;
    private String address;
    private String tel;
    private String storeTypeCd;
    private String nearByLandmarks;
    private String mapX;
    private String mapY;
    private String artificialPerson;
    private String establishment;
    private String businessScope;
    private String storeName;
    private String storeTypeName;
    private String createTime;
    private List<StoreAttrDto> storeAttrDtoList;

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

    public String getNearByLandmarks() {
        return nearByLandmarks;
    }

    public void setNearByLandmarks(String nearByLandmarks) {
        this.nearByLandmarks = nearByLandmarks;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreTypeName() {
        return storeTypeName;
    }

    public void setStoreTypeName(String storeTypeName) {
        this.storeTypeName = storeTypeName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getArtificialPerson() {
        return artificialPerson;
    }

    public void setArtificialPerson(String artificialPerson) {
        this.artificialPerson = artificialPerson;
    }

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public List<StoreAttrDto> getStoreAttrDtoList() {
        return storeAttrDtoList;
    }

    public void setStoreAttrDtoList(List<StoreAttrDto> storeAttrDtoList) {
        this.storeAttrDtoList = storeAttrDtoList;
    }
}
