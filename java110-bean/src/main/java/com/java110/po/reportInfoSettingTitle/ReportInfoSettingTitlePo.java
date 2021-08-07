package com.java110.po.reportInfoSettingTitle;

import java.io.Serializable;
import java.util.Date;

public class ReportInfoSettingTitlePo implements Serializable {

    private String titleType;
private String titleId;
private String statusCd = "0";
private String title;
private String communityId;
private String seq;
private String settingId;
public String getTitleType() {
        return titleType;
    }
public void setTitleType(String titleType) {
        this.titleType = titleType;
    }
public String getTitleId() {
        return titleId;
    }
public void setTitleId(String titleId) {
        this.titleId = titleId;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getTitle() {
        return title;
    }
public void setTitle(String title) {
        this.title = title;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getSeq() {
        return seq;
    }
public void setSeq(String seq) {
        this.seq = seq;
    }
public String getSettingId() {
        return settingId;
    }
public void setSettingId(String settingId) {
        this.settingId = settingId;
    }



}
