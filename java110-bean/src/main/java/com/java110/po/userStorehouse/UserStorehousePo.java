package com.java110.po.userStorehouse;

import java.io.Serializable;

public class UserStorehousePo implements Serializable {
    private String resName;
    private String storeId;
    private String stock;
    private String resId;
    private String resCode;
    private String rstId;
    private String userId;
    private String usId;
    private String statusCd = "0";
    private String unitCode;
    private String unitCodeName;
    private String miniUnitCode;
    private String miniUnitCodeName;
    private String miniUnitStock;
    //最小计量总数
    private String miniStock;
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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getRstId() {
        return rstId;
    }

    public void setRstId(String rstId) {
        this.rstId = rstId;
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

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
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
