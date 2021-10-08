package com.java110.po.store;

import java.io.Serializable;

/**
 * @ClassName StoreUserPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 22:30
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class StoreUserPo implements Serializable {

    private String storeUserId;
    private String storeId;
    private String userId;
    private String relCd;
    private String bId;
    private String statusCd = "0";

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

    public String getRelCd() {
        return relCd;
    }

    public void setRelCd(String relCd) {
        this.relCd = relCd;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
