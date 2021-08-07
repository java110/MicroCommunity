package com.java110.po.reportInfoAnswerValue;

import java.io.Serializable;
import java.util.Date;

public class ReportInfoAnswerValuePo implements Serializable {

    private String valueId;
private String userAnId;
private String titleId;
private String anValueId;
private String valueContent;
private String statusCd = "0";
private String communityId;
private String settingId;
public String getValueId() {
        return valueId;
    }
public void setValueId(String valueId) {
        this.valueId = valueId;
    }
public String getUserAnId() {
        return userAnId;
    }
public void setUserAnId(String userAnId) {
        this.userAnId = userAnId;
    }
public String getTitleId() {
        return titleId;
    }
public void setTitleId(String titleId) {
        this.titleId = titleId;
    }
public String getAnValueId() {
        return anValueId;
    }
public void setAnValueId(String anValueId) {
        this.anValueId = anValueId;
    }
public String getValueContent() {
        return valueContent;
    }
public void setValueContent(String valueContent) {
        this.valueContent = valueContent;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getSettingId() {
        return settingId;
    }
public void setSettingId(String settingId) {
        this.settingId = settingId;
    }



}
