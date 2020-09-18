package com.java110.po.contractTypeSpec;

import java.io.Serializable;
import java.util.Date;

public class ContractTypeSpecPo implements Serializable {

    private String specType;
private String specName;
private String specHoldplace;
private String specValueType;
private String specCd;
private String statusCd = "0";
private String storeId;
private String specShow;
private String contractTypeId;
private String required;
private String listShow;
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
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getSpecShow() {
        return specShow;
    }
public void setSpecShow(String specShow) {
        this.specShow = specShow;
    }
public String getContractTypeId() {
        return contractTypeId;
    }
public void setContractTypeId(String contractTypeId) {
        this.contractTypeId = contractTypeId;
    }
public String getRequired() {
        return required;
    }
public void setRequired(String required) {
        this.required = required;
    }
public String getListShow() {
        return listShow;
    }
public void setListShow(String listShow) {
        this.listShow = listShow;
    }



}
