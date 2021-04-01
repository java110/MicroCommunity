package com.java110.po.parkingSpaceAttr;

import java.io.Serializable;
import java.util.Date;

public class ParkingSpaceAttrPo implements Serializable {

    private String attrId;
private String psId;
private String specCd;
private String communityId;
private String value;
public String getAttrId() {
        return attrId;
    }
public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
public String getPsId() {
        return psId;
    }
public void setPsId(String psId) {
        this.psId = psId;
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



}
