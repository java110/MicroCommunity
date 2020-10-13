package com.java110.po.productDetail;

import java.io.Serializable;
import java.util.Date;

public class ProductDetailPo implements Serializable {

    private String productId;
private String detailId;
private String statusCd = "0";
private String storeId;
private String content;
public String getProductId() {
        return productId;
    }
public void setProductId(String productId) {
        this.productId = productId;
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
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getContent() {
        return content;
    }
public void setContent(String content) {
        this.content = content;
    }



}
