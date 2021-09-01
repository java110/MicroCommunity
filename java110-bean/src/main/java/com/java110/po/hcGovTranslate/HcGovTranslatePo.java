package com.java110.po.hcGovTranslate;

import java.io.Serializable;
import java.util.Date;

public class HcGovTranslatePo implements Serializable {

    private String tranId;
private String code;
private String serviceCode;
private String sign;
private String updateTime;
private String remark;
private String statusCd = "0";
private String reqTime;
private String sendCount;
private String extCommunityId;
private String objId;
private String state;
private String communityId;
private String govTopic;
public String getTranId() {
        return tranId;
    }
public void setTranId(String tranId) {
        this.tranId = tranId;
    }
public String getCode() {
        return code;
    }
public void setCode(String code) {
        this.code = code;
    }
public String getServiceCode() {
        return serviceCode;
    }
public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
public String getSign() {
        return sign;
    }
public void setSign(String sign) {
        this.sign = sign;
    }
public String getUpdateTime() {
        return updateTime;
    }
public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getReqTime() {
        return reqTime;
    }
public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }
public String getSendCount() {
        return sendCount;
    }
public void setSendCount(String sendCount) {
        this.sendCount = sendCount;
    }
public String getExtCommunityId() {
        return extCommunityId;
    }
public void setExtCommunityId(String extCommunityId) {
        this.extCommunityId = extCommunityId;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getGovTopic() {
        return govTopic;
    }
public void setGovTopic(String govTopic) {
        this.govTopic = govTopic;
    }



}
