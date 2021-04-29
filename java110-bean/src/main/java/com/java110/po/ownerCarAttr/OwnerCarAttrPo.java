package com.java110.po.ownerCarAttr;

import java.io.Serializable;
import java.util.Date;

public class OwnerCarAttrPo implements Serializable {

    private String attrId;
private String specCd;
private String communityId;
private String value;
private String carId;
public String getAttrId() {
        return attrId;
    }
public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
public String getSpecCd() {
        return specCd;
    }
public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getValue() {
        return value;
    }
public void setValue(String value) {
        this.value = value;
    }
public String getCarId() {
        return carId;
    }
public void setCarId(String carId) {
        this.carId = carId;
    }



}
