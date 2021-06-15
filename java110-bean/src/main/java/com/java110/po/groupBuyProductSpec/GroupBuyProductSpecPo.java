package com.java110.po.groupBuyProductSpec;

import java.io.Serializable;

public class GroupBuyProductSpecPo implements Serializable {

    private String valueId;
    private String specId;
    private String groupSales;
    private String productId;
    private String groupPrice;
    private String defaultShow;
    private String statusCd = "0";
    private String storeId;
    private String groupStock;

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getGroupSales() {
        return groupSales;
    }

    public void setGroupSales(String groupSales) {
        this.groupSales = groupSales;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(String groupPrice) {
        this.groupPrice = groupPrice;
    }

    public String getDefaultShow() {
        return defaultShow;
    }

    public void setDefaultShow(String defaultShow) {
        this.defaultShow = defaultShow;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getGroupStock() {
        return groupStock;
    }

    public void setGroupStock(String groupStock) {
        this.groupStock = groupStock;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }
}
