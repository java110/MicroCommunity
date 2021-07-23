package com.java110.po.feeCollectionDetail;

import java.io.Serializable;
import java.util.Date;

public class FeeCollectionDetailPo implements Serializable {

    private String orderId;
private String detailId;
private String statusCd = "0";
private String ownerId;
private String collectionName;
private String payerObjName;
private String ownerName;
private String feeName;
private String oweAmount;
private String state;
private String communityId;
private String payerObjType;
private String collectionWay;
private String remarks;
private String payerObjId;
public String getOrderId() {
        return orderId;
    }
public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getOwnerId() {
        return ownerId;
    }
public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
public String getCollectionName() {
        return collectionName;
    }
public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
public String getPayerObjName() {
        return payerObjName;
    }
public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
    }
public String getOwnerName() {
        return ownerName;
    }
public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
public String getFeeName() {
        return feeName;
    }
public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
public String getOweAmount() {
        return oweAmount;
    }
public void setOweAmount(String oweAmount) {
        this.oweAmount = oweAmount;
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
public String getPayerObjType() {
        return payerObjType;
    }
public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
    }
public String getCollectionWay() {
        return collectionWay;
    }
public void setCollectionWay(String collectionWay) {
        this.collectionWay = collectionWay;
    }
public String getRemarks() {
        return remarks;
    }
public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
public String getPayerObjId() {
        return payerObjId;
    }
public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
    }



}
