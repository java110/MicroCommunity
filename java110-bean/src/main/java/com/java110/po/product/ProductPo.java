package com.java110.po.product;

import java.io.Serializable;
import java.util.Date;

public class ProductPo implements Serializable {

    private String productId;
private String unitName;
private String isPostage;
private String statusCd = "0";
private String sort;
private String storeId;
private String barCode;
private String postage;
private String prodName;
private String state;
private String keyword;
private String prodDesc;
private String categoryId;
public String getProductId() {
        return productId;
    }
public void setProductId(String productId) {
        this.productId = productId;
    }
public String getUnitName() {
        return unitName;
    }
public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
public String getIsPostage() {
        return isPostage;
    }
public void setIsPostage(String isPostage) {
        this.isPostage = isPostage;
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
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getBarCode() {
        return barCode;
    }
public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
public String getPostage() {
        return postage;
    }
public void setPostage(String postage) {
        this.postage = postage;
    }
public String getProdName() {
        return prodName;
    }
public void setProdName(String prodName) {
        this.prodName = prodName;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getKeyword() {
        return keyword;
    }
public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
public String getProdDesc() {
        return prodDesc;
    }
public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }
public String getCategoryId() {
        return categoryId;
    }
public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }



}
