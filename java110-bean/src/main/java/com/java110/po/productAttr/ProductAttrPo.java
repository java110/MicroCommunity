package com.java110.po.productAttr;

import java.io.Serializable;
import java.util.Date;

public class ProductAttrPo implements Serializable {

    private String attrId;
private String productId;
private String specCd;
private String statusCd = "0";
private String storeId;
private String value;
public String getAttrId() {
        return attrId;
    }
public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
public String getProductId() {
        return productId;
    }
public void setProductId(String productId) {
        this.productId = productId;
    }
public String getSpecCd() {
        return specCd;
    }
public void setSpecCd(String specCd) {
        this.specCd = specCd;
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
public String getValue() {
        return value;
    }
public void setValue(String value) {
        this.value = value;
    }



}
