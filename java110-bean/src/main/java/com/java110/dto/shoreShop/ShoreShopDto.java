package com.java110.dto.shoreShop;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 店铺数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ShoreShopDto extends PageDto implements Serializable {

    private String shopDesc;
private String returnPerson;
private String shopLogo;
private String shopName;
private String storeId;
private String mapY;
private String mapX;
private String sendAddress;
private String returnAddress;
private String openType;
private String areaCode;
private String returnLink;
private String shopId;
private String state;
private String shopType;


    private Date createTime;

    private String statusCd = "0";


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
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getMapY() {
        return mapY;
    }
public void setMapY(String mapY) {
        this.mapY = mapY;
    }
public String getMapX() {
        return mapX;
    }
public void setMapX(String mapX) {
        this.mapX = mapX;
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
public String getOpenType() {
        return openType;
    }
public void setOpenType(String openType) {
        this.openType = openType;
    }
public String getAreaCode() {
        return areaCode;
    }
public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getShopType() {
        return shopType;
    }
public void setShopType(String shopType) {
        this.shopType = shopType;
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
