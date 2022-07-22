package com.java110.po.attrValue;

import java.io.Serializable;

public class AttrValuePo implements Serializable {

    private String valueId;
    private String valueName;
    private String valueShow;
    private String specCd;
    private String specId;
    private String statusCd = "0";
    private String value;

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValueShow() {
        return valueShow;
    }

    public void setValueShow(String valueShow) {
        this.valueShow = valueShow;
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

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }
}
