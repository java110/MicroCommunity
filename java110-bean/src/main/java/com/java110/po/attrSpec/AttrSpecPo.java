package com.java110.po.attrSpec;

import java.io.Serializable;

public class AttrSpecPo implements Serializable {

    private String specType;
    private String specName;
    private String specHoldplace;
    private String specValueType;
    private String specCd;
    private String specId;
    private String statusCd = "0";
    private String specShow;
    private String required;
    private String tableName;
    private String listShow;
    private String domain;



    public String getSpecType() {
        return specType;
    }

    public void setSpecType(String specType) {
        this.specType = specType;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecHoldplace() {
        return specHoldplace;
    }

    public void setSpecHoldplace(String specHoldplace) {
        this.specHoldplace = specHoldplace;
    }

    public String getSpecValueType() {
        return specValueType;
    }

    public void setSpecValueType(String specValueType) {
        this.specValueType = specValueType;
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

    public String getSpecShow() {
        return specShow;
    }

    public void setSpecShow(String specShow) {
        this.specShow = specShow;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getListShow() {
        return listShow;
    }

    public void setListShow(String listShow) {
        this.listShow = listShow;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }
}
