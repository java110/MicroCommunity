package com.java110.dto.workflowAuditMessage;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 流程审核表数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WorkflowAuditMessageDto extends PageDto implements Serializable {

    //分片类型 2020 根据小区分片 3030 根据商户ID
    public static final String SHARE_TYPE_COMMUNITY = "2020";
    public static final String SHARE_TYPE_STORE = "3030";
    public static final String BUSINESS_TYPE_SHOP = "001"; //商铺

    public static final String STATE_Y = "1100"; // 同意
    public static final String STATE_N = "1200"; //不统一

    private String auditId;
    private String preAuditId;
    private String businessKey;
    private String auditPersonId;
    private String auditPerson;
    private String shareId;
    private String state;
    private String businessType;
    private String message;
    private String shareType;
    private String auditLink;


    private Date createTime;

    private String statusCd = "0";


    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public String getPreAuditId() {
        return preAuditId;
    }

    public void setPreAuditId(String preAuditId) {
        this.preAuditId = preAuditId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getAuditPersonId() {
        return auditPersonId;
    }

    public void setAuditPersonId(String auditPersonId) {
        this.auditPersonId = auditPersonId;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getAuditLink() {
        return auditLink;
    }

    public void setAuditLink(String auditLink) {
        this.auditLink = auditLink;
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
