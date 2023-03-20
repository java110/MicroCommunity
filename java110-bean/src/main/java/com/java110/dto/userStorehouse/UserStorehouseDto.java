package com.java110.dto.userStorehouse;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 个人物品数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class UserStorehouseDto extends PageDto implements Serializable {

    private String resName;
    private String storeId;
    private String stock;
    private String resId;
    private String userId;
    private String usId;
    private String resCode;
    private String rstId;
    private String parentRstId;
    private String outLowPrice;
    private String outHighPrice;
    private String rstName;
    private String parentRstName;
    private String specName;
    private String rssId;
    private String unitCode;
    private String unitCodeName;
    private String miniUnitCode;
    private String miniUnitCodeName;
    private String miniUnitStock;
    private String userName;
    //最小计量总数
    private String miniStock;
    private Date createTime;
    private String statusCd = "0";

    private String isFixed;
    private String isFixedName;
    private String lagerStockZero = "0";
    private String timesId;

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsId() {
        return usId;
    }

    public void setUsId(String usId) {
        this.usId = usId;
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

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getOutLowPrice() {
        return outLowPrice;
    }

    public void setOutLowPrice(String outLowPrice) {
        this.outLowPrice = outLowPrice;
    }

    public String getOutHighPrice() {
        return outHighPrice;
    }

    public void setOutHighPrice(String outHighPrice) {
        this.outHighPrice = outHighPrice;
    }

    public String getRstId() {
        return rstId;
    }

    public void setRstId(String rstId) {
        this.rstId = rstId;
    }

    public String getRstName() {
        return rstName;
    }

    public void setRstName(String rstName) {
        this.rstName = rstName;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getRssId() {
        return rssId;
    }

    public void setRssId(String rssId) {
        this.rssId = rssId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitCodeName() {
        return unitCodeName;
    }

    public void setUnitCodeName(String unitCodeName) {
        this.unitCodeName = unitCodeName;
    }

    public String getMiniUnitCode() {
        return miniUnitCode;
    }

    public void setMiniUnitCode(String miniUnitCode) {
        this.miniUnitCode = miniUnitCode;
    }

    public String getMiniUnitCodeName() {
        return miniUnitCodeName;
    }

    public void setMiniUnitCodeName(String miniUnitCodeName) {
        this.miniUnitCodeName = miniUnitCodeName;
    }

    public String getMiniStock() {
        return miniStock;
    }

    public void setMiniStock(String miniStock) {
        this.miniStock = miniStock;
    }

    public String getMiniUnitStock() {
        return miniUnitStock;
    }

    public void setMiniUnitStock(String miniUnitStock) {
        this.miniUnitStock = miniUnitStock;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParentRstName() {
        return parentRstName;
    }

    public void setParentRstName(String parentRstName) {
        this.parentRstName = parentRstName;
    }

    public String getParentRstId() {
        return parentRstId;
    }

    public void setParentRstId(String parentRstId) {
        this.parentRstId = parentRstId;
    }

    public String getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(String isFixed) {
        this.isFixed = isFixed;
    }

    public String getIsFixedName() {
        return isFixedName;
    }

    public void setIsFixedName(String isFixedName) {
        this.isFixedName = isFixedName;
    }

    public String getLagerStockZero() {
        return lagerStockZero;
    }

    public void setLagerStockZero(String lagerStockZero) {
        this.lagerStockZero = lagerStockZero;
    }

    public String getTimesId() {
        return timesId;
    }

    public void setTimesId(String timesId) {
        this.timesId = timesId;
    }
}
