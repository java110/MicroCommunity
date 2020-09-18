package com.java110.po.contractType;

import java.io.Serializable;
import java.util.Date;

public class ContractTypePo implements Serializable {

    private String audit;
private String typeName;
private String remark;
private String statusCd = "0";
private String storeId;
private String contractTypeId;
public String getAudit() {
        return audit;
    }
public void setAudit(String audit) {
        this.audit = audit;
    }
public String getTypeName() {
        return typeName;
    }
public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
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
public String getContractTypeId() {
        return contractTypeId;
    }
public void setContractTypeId(String contractTypeId) {
        this.contractTypeId = contractTypeId;
    }



}
