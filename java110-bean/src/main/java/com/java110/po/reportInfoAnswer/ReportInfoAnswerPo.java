package com.java110.po.reportInfoAnswer;

import java.io.Serializable;
import java.util.Date;

public class ReportInfoAnswerPo implements Serializable {

    private String personName;
private String userAnId;
private String personId;
private String statusCd = "0";
private String communityId;
private String settingId;
public String getPersonName() {
        return personName;
    }
public void setPersonName(String personName) {
        this.personName = personName;
    }
public String getUserAnId() {
        return userAnId;
    }
public void setUserAnId(String userAnId) {
        this.userAnId = userAnId;
    }
public String getPersonId() {
        return personId;
    }
public void setPersonId(String personId) {
        this.personId = personId;
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
