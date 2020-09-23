package com.java110.po.feeManualCollectionDetail;

import java.io.Serializable;
import java.util.Date;

public class FeeManualCollectionDetailPo implements Serializable {

    private String amount;
private String feeName;
private String detailId;
private String startTime;
private String statusCd = "0";
private String endTime;
private String state;
private String communityId;
private String collectionId;
private String feeId;
public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getFeeName() {
        return feeName;
    }
public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getStartTime() {
        return startTime;
    }
public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
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
public String getCollectionId() {
        return collectionId;
    }
public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }
public String getFeeId() {
        return feeId;
    }
public void setFeeId(String feeId) {
        this.feeId = feeId;
    }



}
