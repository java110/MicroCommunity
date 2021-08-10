package com.java110.po.assetImportLogDetail;

import java.io.Serializable;
import java.util.Date;

public class AssetImportLogDetailPo implements Serializable {

    private String detailId;
private String logId;
private String statusCd = "0";
private String state;
private String objName;
private String communityId;
private String message;
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getLogId() {
        return logId;
    }
public void setLogId(String logId) {
        this.logId = logId;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getObjName() {
        return objName;
    }
public void setObjName(String objName) {
        this.objName = objName;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getMessage() {
        return message;
    }
public void setMessage(String message) {
        this.message = message;
    }



}
