package com.java110.dto.activities;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 活动规则数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ActivitiesBeautifulStaffDto extends PageDto implements Serializable {

    private String beId;
    private String workContent;
    private String activitiesNum;
    private String poll;
    private String ruleId;
    private String ruleName;
    private String storeId;
    private String staffId;
    private String staffName;


    private Date createTime;

    private String statusCd = "0";


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


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
