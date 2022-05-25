package com.java110.dto.store;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 业主数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class StoreDto extends PageDto implements Serializable {

    //运营团队store_id
    public static final String STORE_ADMIN = "400000000000000001";

    //开发store_id
    public static final String STORE_DEV = "400000000000000002";
    //运营
    public static final String STORE_TYPE_ADMIN = "800900000001";
    //开发
    public static final String STORE_TYPE_DEV = "800900000000";
    //物业商户
    public static final String STORE_TYPE_PROPERTY = "800900000003";
    //商家
    public static final String STORE_TYPE_MALL = "800900000005";

    public static final String STATE_NORMAL = "48001";
    public static final String STATE_LOGIN_FAIL = "48001";

    private String storeId;
    private String[] storeIds;
    private String userId;
    private String name;
    private String address;
    private String tel;
    private String storeTypeCd;
    private String nearByLandmarks;
    private String mapX;
    private String mapY;
    private String storeName;
    private String storeTypeName;
    private String corporation;
    private String foundingTime;
    private String state;

    private Date createTime;

    private String statusCd = "0";

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

    public String getStoreTypeName() {
        return storeTypeName;
    }

    public void setStoreTypeName(String storeTypeName) {
        this.storeTypeName = storeTypeName;
    }

    public String[] getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(String[] storeIds) {
        this.storeIds = storeIds;
    }

    public String getCorporation() {
        return corporation;
    }

    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    public String getFoundingTime() {
        return foundingTime;
    }

    public void setFoundingTime(String foundingTime) {
        this.foundingTime = foundingTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
