package com.java110.po.userStorehouse;

import java.io.Serializable;
import java.util.Date;

public class UserStorehousePo implements Serializable {

    private String resName;
private String storeId;
private String stock;
private String resId;
private String userId;
private String usId;
public String getResName() {
        return resName;
    }
public void setResName(String resName) {
        this.resName = resName;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getStock() {
        return stock;
    }
public void setStock(String stock) {
        this.stock = stock;
    }
public String getResId() {
        return resId;
    }
public void setResId(String resId) {
        this.resId = resId;
    }
public String getUserId() {
        return userId;
    }
public void setUserId(String userId) {
        this.userId = userId;
    }
public String getUsId() {
        return usId;
    }
public void setUsId(String usId) {
        this.usId = usId;
    }



}
