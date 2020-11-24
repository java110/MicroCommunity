package com.java110.po.feeDiscountRule;

import java.io.Serializable;
import java.util.Date;

public class FeeDiscountRulePo implements Serializable {

    private String ruleName;
private String remark;
private String statusCd = "0";
private String ruleId;
private String beanImpl;
public String getRuleName() {
        return ruleName;
    }
public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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
public String getBeanImpl() {
        return beanImpl;
    }
public void setBeanImpl(String beanImpl) {
        this.beanImpl = beanImpl;
    }



}
