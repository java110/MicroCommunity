package com.java110.po.feePrintSpec;

import java.io.Serializable;
import java.util.Date;

public class FeePrintSpecPo implements Serializable {

    private String printId;
private String context;
private String specCd;
private String statusCd = "0";
private String communityId;
private String qrImg;
public String getPrintId() {
        return printId;
    }
public void setPrintId(String printId) {
        this.printId = printId;
    }
public String getContext() {
        return context;
    }
public void setContext(String context) {
        this.context = context;
    }
public String getSpecCd() {
        return specCd;
    }
public void setSpecCd(String specCd) {
        this.specCd = specCd;
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
public String getQrImg() {
        return qrImg;
    }
public void setQrImg(String qrImg) {
        this.qrImg = qrImg;
    }



}
