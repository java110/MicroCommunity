package com.java110.po.product;

import java.io.Serializable;
import java.util.Date;

public class ProductSpecDetailPo implements Serializable {

    private String specId;
private String detailName;
private String detailValue;
private String detailId;
private String statusCd = "0";
private String storeId;
public String getSpecId() {
        return specId;
    }
public void setSpecId(String specId) {
        this.specId = specId;
    }
public String getDetailName() {
        return detailName;
    }
public void setDetailName(String detailName) {
        this.detailName = detailName;
    }
public String getDetailValue() {
        return detailValue;
    }
public void setDetailValue(String detailValue) {
        this.detailValue = detailValue;
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



}
