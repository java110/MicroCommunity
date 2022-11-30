package com.java110.dto.shop;

import com.java110.dto.PageDto;
import com.java110.dto.distributionMode.DistributionModeDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 店铺数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ShopDto extends PageDto implements Serializable {

    public static final String STATE_APPLY = "001";
    public static final String STATE_Y = "002";
    public static final String STATE_N = "003";
    public static final String STATE_B = "004";
    public static final String STATE_F = "005"; //已交保证金
    public static final String OPEN_TYPE_SHOP = "1";//商家
    private String shopDesc;
    private String returnPerson;
    private String shopLogo;
    private String shopName;
    private String returnLink;
    private String shopId;

    private String storeId;
    private String storeName;
    private String storeTel;
    private String storeAddress;
    private String sendAddress;
    private String returnAddress;
    private String shopType;
    private String openType;
    private String[] openTypes;
    private String shopTypeName;
    private String state;
    private String[] states;
    private String stateName;
    private String isPayUp;
    private String phoneIndexUrl;
    private String areaCode;
    private String areaName;
    private String mapX;
    private String mapY;
    private String[] notInShopId;
    private List<DistributionModeDto> distributionModeDtos;

    private Date createTime;

    private String statusCd = "0";

    //排序标识
    private String flag;

    private String modeName;

    private String serviceFee;

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getReturnPerson() {
        return returnPerson;
    }

    public void setReturnPerson(String returnPerson) {
        this.returnPerson = returnPerson;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getReturnLink() {
        return returnLink;
    }

    public void setReturnLink(String returnLink) {
        this.returnLink = returnLink;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
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

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreTel() {
        return storeTel;
    }

    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public String[] getOpenTypes() {
        return openTypes;
    }

    public void setOpenTypes(String[] openTypes) {
        this.openTypes = openTypes;
    }

    public String getIsPayUp() {
        return isPayUp;
    }

    public void setIsPayUp(String isPayUp) {
        this.isPayUp = isPayUp;
    }

    public String getPhoneIndexUrl() {
        return phoneIndexUrl;
    }

    public void setPhoneIndexUrl(String phoneIndexUrl) {
        this.phoneIndexUrl = phoneIndexUrl;
    }

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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public List<DistributionModeDto> getDistributionModeDtos() {
        return distributionModeDtos;
    }

    public void setDistributionModeDtos(List<DistributionModeDto> distributionModeDtos) {
        this.distributionModeDtos = distributionModeDtos;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String[] getNotInShopId() {
        return notInShopId;
    }

    public void setNotInShopId(String[] notInShopId) {
        this.notInShopId = notInShopId;
    }
}
