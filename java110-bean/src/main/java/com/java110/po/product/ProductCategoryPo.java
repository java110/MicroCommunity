package com.java110.po.product;

import java.io.Serializable;
import java.util.Date;

public class ProductCategoryPo implements Serializable {

    private String categoryLevel;
private String parentCategoryId;
private String statusCd = "0";
private String storeId;
private String categoryName;
private String categoryId;
private String seq;
private String isShow;
public String getCategoryLevel() {
        return categoryLevel;
    }
public void setCategoryLevel(String categoryLevel) {
        this.categoryLevel = categoryLevel;
    }
public String getParentCategoryId() {
        return parentCategoryId;
    }
public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
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
public String getCategoryName() {
        return categoryName;
    }
public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
public String getCategoryId() {
        return categoryId;
    }
public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
public String getSeq() {
        return seq;
    }
public void setSeq(String seq) {
        this.seq = seq;
    }
public String getIsShow() {
        return isShow;
    }
public void setIsShow(String isShow) {
        this.isShow = isShow;
    }



}
