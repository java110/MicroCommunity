package com.java110.po.store;

import java.io.Serializable;

/**
 * @ClassName StoreAttrPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 22:05
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class StoreAttrPo implements Serializable {

    private String attrId;
    private String storeId;
    private String specCd;
    private String value;
    private String statusCd = "0";


    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSpecCd() {
        return specCd;
    }

    public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
