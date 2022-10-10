package com.java110.dto.storeShopCommunity;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 小区店铺数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class StoreShopCommunityDto extends PageDto implements Serializable {

    private String address;
private String cityCode;
private String codeName;
private String communityName;
private String startTime;
private String shopId;
private String endTime;
private String state;
private String communityId;
private String message;
private String scId;


    private Date createTime;

    private String statusCd = "0";


    public String getAddress() {
        return address;
    }
public void setAddress(String address) {
        this.address = address;
    }
public String getCityCode() {
        return cityCode;
    }
public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
public String getCodeName() {
        return codeName;
    }
public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
public String getCommunityName() {
        return communityName;
    }
public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
public String getStartTime() {
        return startTime;
    }
public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
public String getShopId() {
        return shopId;
    }
public void setShopId(String shopId) {
        this.shopId = shopId;
    }
public String getEndTime() {
        return endTime;
    }
public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getMessage() {
        return message;
    }
public void setMessage(String message) {
        this.message = message;
    }
public String getScId() {
        return scId;
    }
public void setScId(String scId) {
        this.scId = scId;
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
