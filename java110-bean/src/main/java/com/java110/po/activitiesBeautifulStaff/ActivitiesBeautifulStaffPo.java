package com.java110.po.activitiesBeautifulStaff;

import java.io.Serializable;
import java.util.Date;

public class ActivitiesBeautifulStaffPo implements Serializable {

    private String beId;
private String workContent;
private String activitiesNum;
private String statusCd = "0";
private String poll;
private String ruleId;
private String storeId;
private String staffId;
public String getBeId() {
        return beId;
    }
public void setBeId(String beId) {
        this.beId = beId;
    }
public String getWorkContent() {
        return workContent;
    }
public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }
public String getActivitiesNum() {
        return activitiesNum;
    }
public void setActivitiesNum(String activitiesNum) {
        this.activitiesNum = activitiesNum;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getPoll() {
        return poll;
    }
public void setPoll(String poll) {
        this.poll = poll;
    }
public String getRuleId() {
        return ruleId;
    }
public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getStaffId() {
        return staffId;
    }
public void setStaffId(String staffId) {
        this.staffId = staffId;
    }



}
