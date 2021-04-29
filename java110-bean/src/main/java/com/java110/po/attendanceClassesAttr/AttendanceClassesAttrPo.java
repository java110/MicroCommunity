package com.java110.po.attendanceClassesAttr;

import java.io.Serializable;
import java.util.Date;

public class AttendanceClassesAttrPo implements Serializable {

    private String classesId;
private String attrId;
private String specCd;
private String storeId;
private String value;
public String getClassesId() {
        return classesId;
    }
public void setClassesId(String classesId) {
        this.classesId = classesId;
    }
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
