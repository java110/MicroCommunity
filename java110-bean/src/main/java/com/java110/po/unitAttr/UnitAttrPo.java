package com.java110.po.unitAttr;

import java.io.Serializable;
import java.util.Date;

public class UnitAttrPo implements Serializable {

    private String attrId;
private String unitId;
private String specCd;
private String value;
public String getAttrId() {
        return attrId;
    }
public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
public String getUnitId() {
        return unitId;
    }
public void setUnitId(String unitId) {
        this.unitId = unitId;
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
