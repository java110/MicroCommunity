package com.java110.dto.smsConfig;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 短信配置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class SmsConfigDto extends PageDto implements Serializable {

    //短信业务 1001 欠费催缴 2002 工单通知
    public static final String SMS_BUSI_OWE = "1001";
    public static final String SMS_BUSI_ORDER_NOTICE = "2002";

    private String accessKeyId;
    private String smsBusi;
    private String signName;
    private String templateCode;
    private String storeId;
    private String accessSecret;
    private String smsId;
    private String objId;
    private String smsType;
    private String logSwitch;
    private String region;
    private String remarks;


    private Date createTime;

    private String statusCd = "0";


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


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
