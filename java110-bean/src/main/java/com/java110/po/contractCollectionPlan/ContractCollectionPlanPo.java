package com.java110.po.contractCollectionPlan;

import java.io.Serializable;
import java.util.Date;

public class ContractCollectionPlanPo implements Serializable {

    private String contractId;
private String feeName;
private String planName;
private String planId;
private String remark;
private String statusCd = "0";
private String storeId;
private String feeId;
public String getContractId() {
        return contractId;
    }
public void setContractId(String contractId) {
        this.contractId = contractId;
    }
public String getFeeName() {
        return feeName;
    }
public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
public String getPlanName() {
        return planName;
    }
public void setPlanName(String planName) {
        this.planName = planName;
    }
public String getPlanId() {
        return planId;
    }
public void setPlanId(String planId) {
        this.planId = planId;
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
public String getFeeId() {
        return feeId;
    }
public void setFeeId(String feeId) {
        this.feeId = feeId;
    }



}
