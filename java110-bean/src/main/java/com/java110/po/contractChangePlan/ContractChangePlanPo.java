package com.java110.po.contractChangePlan;

import java.io.Serializable;

public class ContractChangePlanPo implements Serializable {

    private String planType;
    private String contractId;
    private String planId;
    private String remark;
    private String statusCd = "0";
    private String changePerson;
    private String state;
    private String storeId;



    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
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

    public String getChangePerson() {
        return changePerson;
    }

    public void setChangePerson(String changePerson) {
        this.changePerson = changePerson;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

}
