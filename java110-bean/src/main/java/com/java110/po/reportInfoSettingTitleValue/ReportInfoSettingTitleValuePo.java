package com.java110.po.reportInfoSettingTitleValue;

import java.io.Serializable;
import java.util.Date;

public class ReportInfoSettingTitleValuePo implements Serializable {

    private String valueId;
private String titleId;
private String statusCd = "0";
private String communityId;
private String qaValue;
private String seq;
public String getValueId() {
        return valueId;
    }
public void setValueId(String valueId) {
        this.valueId = valueId;
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
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getQaValue() {
        return qaValue;
    }
public void setQaValue(String qaValue) {
        this.qaValue = qaValue;
    }
public String getSeq() {
        return seq;
    }
public void setSeq(String seq) {
        this.seq = seq;
    }



}
