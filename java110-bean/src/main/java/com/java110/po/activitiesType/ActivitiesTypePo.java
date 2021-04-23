package com.java110.po.activitiesType;

import java.io.Serializable;
import java.util.Date;

public class ActivitiesTypePo implements Serializable {

    private String typeDesc;
private String typeCd;
private String typeName;
private String defaultShow;
private String statusCd = "0";
private String communityId;
private String seq;
public String getTypeDesc() {
        return typeDesc;
    }
public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
public String getTypeCd() {
        return typeCd;
    }
public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }
public String getTypeName() {
        return typeName;
    }
public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
public String getDefaultShow() {
        return defaultShow;
    }
public void setDefaultShow(String defaultShow) {
        this.defaultShow = defaultShow;
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
public String getSeq() {
        return seq;
    }
public void setSeq(String seq) {
        this.seq = seq;
    }



}
