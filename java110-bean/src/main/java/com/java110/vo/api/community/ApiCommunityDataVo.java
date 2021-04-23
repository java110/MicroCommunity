package com.java110.vo.api.community;

import com.java110.dto.community.CommunityAttrDto;

import java.io.Serializable;
import java.util.List;

public class ApiCommunityDataVo implements Serializable {

    private String communityMemberId;
    private String communityId;
    private String name;
    private String address;
    private String nearbyLandmarks;
    private String cityCode;
    private String cityName;
    private String mapX;
    private String mapY;
    private String state;
    private String auditStatusCd;
    private String stateName;
    private String memberId;
    private String storeName;
    private String storeTypeCd;
    private String storeTypeName;
    private String tel;

    private List<CommunityAttrDto> communityAttrDtos;

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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreTypeCd() {
        return storeTypeCd;
    }

    public void setStoreTypeCd(String storeTypeCd) {
        this.storeTypeCd = storeTypeCd;
    }

    public String getStoreTypeName() {
        return storeTypeName;
    }

    public void setStoreTypeName(String storeTypeName) {
        this.storeTypeName = storeTypeName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCommunityMemberId() {
        return communityMemberId;
    }

    public void setCommunityMemberId(String communityMemberId) {
        this.communityMemberId = communityMemberId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAuditStatusCd() {
        return auditStatusCd;
    }

    public void setAuditStatusCd(String auditStatusCd) {
        this.auditStatusCd = auditStatusCd;
    }

    public List<CommunityAttrDto> getCommunityAttrDtos() {
        return communityAttrDtos;
    }

    public void setCommunityAttrDtos(List<CommunityAttrDto> communityAttrDtos) {
        this.communityAttrDtos = communityAttrDtos;
    }
}
