package com.java110.po.productLabel;

import java.io.Serializable;

public class ProductLabelPo implements Serializable {

    private String labelId;
    private String productId;
    private String statusCd = "0";
    private String storeId;
    private String labelCd;

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getLabelCd() {
        return labelCd;
    }

    public void setLabelCd(String labelCd) {
        this.labelCd = labelCd;
    }


}
