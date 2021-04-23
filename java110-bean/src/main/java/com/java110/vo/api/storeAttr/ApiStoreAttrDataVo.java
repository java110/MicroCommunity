package com.java110.vo.api.storeAttr;

import java.io.Serializable;
import java.util.Date;

public class ApiStoreAttrDataVo implements Serializable {

    private String attrId;
private String operate;
private String createTime;
private String specCd;
private String statusCd;
private String storeId;
private String bId;
private String value;
public String getAttrId() {
        return attrId;
    }
public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
public String getOperate() {
        return operate;
    }
public void setOperate(String operate) {
        this.operate = operate;
    }
public String getCreateTime() {
        return createTime;
    }
public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
public String getBId() {
        return bId;
    }
public void setBId(String bId) {
        this.bId = bId;
    }
public String getValue() {
        return value;
    }
public void setValue(String value) {
        this.value = value;
    }



}
