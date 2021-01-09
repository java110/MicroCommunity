package com.java110.po.tempCarFeeConfigAttr;

import java.io.Serializable;
import java.util.Date;

public class TempCarFeeConfigAttrPo implements Serializable {

    private String attrId;
private String configId;
private String specCd;
private String value;
public String getAttrId() {
        return attrId;
    }
public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
public String getConfigId() {
        return configId;
    }
public void setConfigId(String configId) {
        this.configId = configId;
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



}
