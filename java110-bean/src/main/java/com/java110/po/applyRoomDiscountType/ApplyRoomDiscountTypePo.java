package com.java110.po.applyRoomDiscountType;

import java.io.Serializable;
import java.util.Date;

public class ApplyRoomDiscountTypePo implements Serializable {

    private String applyType;
private String typeDesc;
private String typeName;
private String statusCd = "0";
private String communityId;
public String getApplyType() {
        return applyType;
    }
public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
public String getTypeDesc() {
        return typeDesc;
    }
public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
public String getTypeName() {
        return typeName;
    }
public void setTypeName(String typeName) {
        this.typeName = typeName;
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



}
