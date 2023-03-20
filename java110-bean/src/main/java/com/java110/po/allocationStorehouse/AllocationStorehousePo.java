package com.java110.po.allocationStorehouse;

import java.io.Serializable;

public class AllocationStorehousePo implements Serializable {

    private String asId;
    private String storeId;
    private String resId;
    private String shIdz;
    private String resName;
    private String startUserId;
    private String shIda;
    private String startUserName;
    private String stock;
    private String remark;
    private String statusCd = "0";
    private String applyId;
    private String originalStock;
    private String timesId;

    public String getAsId() {
        return asId;
    }

    public void setAsId(String asId) {
        this.asId = asId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getShIdz() {
        return shIdz;
    }

    public void setShIdz(String shIdz) {
        this.shIdz = shIdz;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getShIda() {
        return shIda;
    }

    public void setShIda(String shIda) {
        this.shIda = shIda;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }


    public String getOriginalStock() {
        return originalStock;
    }

    public void setOriginalStock(String originalStock) {
        this.originalStock = originalStock;
    }

    public String getTimesId() {
        return timesId;
    }

    public void setTimesId(String timesId) {
        this.timesId = timesId;
    }
}
