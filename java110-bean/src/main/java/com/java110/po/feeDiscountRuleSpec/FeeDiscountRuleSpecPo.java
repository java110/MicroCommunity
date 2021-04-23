package com.java110.po.feeDiscountRuleSpec;

import java.io.Serializable;
import java.util.Date;

public class FeeDiscountRuleSpecPo implements Serializable {

    private String specId;
private String specName;
private String remark;
private String statusCd = "0";
private String ruleId;
public String getSpecId() {
        return specId;
    }
public void setSpecId(String specId) {
        this.specId = specId;
    }
public String getSpecName() {
        return specName;
    }
public void setSpecName(String specName) {
        this.specName = specName;
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
public String getRuleId() {
        return ruleId;
    }
public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }



}
