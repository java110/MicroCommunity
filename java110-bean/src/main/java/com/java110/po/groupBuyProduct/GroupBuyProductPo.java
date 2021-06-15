package com.java110.po.groupBuyProduct;

import java.io.Serializable;

public class GroupBuyProductPo implements Serializable {

    private String groupProdName;
    private String productId;
    private String userCount;
    private String groupId;
    private String groupProdDesc;
    private String statusCd = "0";
    private String sort;
    private String state;
    private String storeId;
    private String batchId;

    public String getGroupProdName() {
        return groupProdName;
    }

    public void setGroupProdName(String groupProdName) {
        this.groupProdName = groupProdName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupProdDesc() {
        return groupProdDesc;
    }

    public void setGroupProdDesc(String groupProdDesc) {
        this.groupProdDesc = groupProdDesc;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }


}
