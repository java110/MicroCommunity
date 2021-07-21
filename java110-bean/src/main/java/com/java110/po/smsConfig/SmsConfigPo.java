package com.java110.po.smsConfig;

import java.io.Serializable;
import java.util.Date;

public class SmsConfigPo implements Serializable {

    private String accessKeyId;
private String smsBusi;
private String signName;
private String statusCd = "0";
private String templateCode;
private String storeId;
private String accessSecret;
private String smsId;
private String objId;
private String smsType;
private String logSwitch;
private String region;
private String remarks;
public String getAccessKeyId() {
        return accessKeyId;
    }
public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
public String getSmsBusi() {
        return smsBusi;
    }
public void setSmsBusi(String smsBusi) {
        this.smsBusi = smsBusi;
    }
public String getSignName() {
        return signName;
    }
public void setSignName(String signName) {
        this.signName = signName;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getTemplateCode() {
        return templateCode;
    }
public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getAccessSecret() {
        return accessSecret;
    }
public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }
public String getSmsId() {
        return smsId;
    }
public void setSmsId(String smsId) {
        this.smsId = smsId;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getSmsType() {
        return smsType;
    }
public void setSmsType(String smsType) {
        this.smsType = smsType;
    }
public String getLogSwitch() {
        return logSwitch;
    }
public void setLogSwitch(String logSwitch) {
        this.logSwitch = logSwitch;
    }
public String getRegion() {
        return region;
    }
public void setRegion(String region) {
        this.region = region;
    }
public String getRemarks() {
        return remarks;
    }
public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



}
