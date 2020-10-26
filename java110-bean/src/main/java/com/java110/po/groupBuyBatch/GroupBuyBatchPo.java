package com.java110.po.groupBuyBatch;

import java.io.Serializable;

public class GroupBuyBatchPo implements Serializable {

    private String batchEndTime;
    private String statusCd = "0";
    private String batchId;
    private String storeId;
    private String settingId;
    private String batchStartTime;
    private String curBatch;
    private String defaultCurBatch;

    public String getBatchEndTime() {
        return batchEndTime;
    }

    public void setBatchEndTime(String batchEndTime) {
        this.batchEndTime = batchEndTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public String getBatchStartTime() {
        return batchStartTime;
    }

    public void setBatchStartTime(String batchStartTime) {
        this.batchStartTime = batchStartTime;
    }

    public String getCurBatch() {
        return curBatch;
    }

    public void setCurBatch(String curBatch) {
        this.curBatch = curBatch;
    }

    public String getDefaultCurBatch() {
        return defaultCurBatch;
    }

    public void setDefaultCurBatch(String defaultCurBatch) {
        this.defaultCurBatch = defaultCurBatch;
    }
}
