package com.java110.po.rentingPoolAttr;

import java.io.Serializable;

public class RentingPoolAttrPo implements Serializable {

    private String attrId;
    private String specCd;
    private String statusCd = "0";
    private String value;
    private String rentingId;

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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRentingId() {
        return rentingId;
    }

    public void setRentingId(String rentingId) {
        this.rentingId = rentingId;
    }


}
