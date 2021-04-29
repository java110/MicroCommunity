package com.java110.po.activitiesRule;

import java.io.Serializable;
import java.util.Date;

public class ActivitiesRulePo implements Serializable {

    private String ruleType;
private String objId;
private String ruleName;
private String activitiesObj;
private String startTime;
private String remark;
private String statusCd = "0";
private String endTime;
private String ruleId;
private String objType;
public String getRuleType() {
        return ruleType;
    }
public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getRuleName() {
        return ruleName;
    }
public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
public String getActivitiesObj() {
        return activitiesObj;
    }
public void setActivitiesObj(String activitiesObj) {
        this.activitiesObj = activitiesObj;
    }
public String getStartTime() {
        return startTime;
    }
public void setStartTime(String startTime) {
        this.startTime = startTime;
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
public String getEndTime() {
        return endTime;
    }
public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
public String getRuleId() {
        return ruleId;
    }
public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
public String getObjType() {
        return objType;
    }
public void setObjType(String objType) {
        this.objType = objType;
    }



}
