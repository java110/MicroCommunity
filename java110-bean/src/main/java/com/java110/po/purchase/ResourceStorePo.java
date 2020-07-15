package com.java110.po.purchase;

import java.io.Serializable;

/**
 * @ClassName ResourceStorePo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 21:34
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class ResourceStorePo implements Serializable {

    private String resId;
    private String storeId;
    private String resName;
    private String resCode;
    private String description;
    private String price;
    private String stock;
    private String statusCd;

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
