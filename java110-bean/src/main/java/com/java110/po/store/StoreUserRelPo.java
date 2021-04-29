package com.java110.po.store;

import java.io.Serializable;

/**
 * @ClassName StoreUserRelPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 13:01
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class StoreUserRelPo implements Serializable {

    private String id;
    private String relCd;
    private String name;
    private String description;

    private String storeUserId;
    private String storeId;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelCd() {
        return relCd;
    }

    public void setRelCd(String relCd) {
        this.relCd = relCd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoreUserId() {
        return storeUserId;
    }

    public void setStoreUserId(String storeUserId) {
        this.storeUserId = storeUserId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
