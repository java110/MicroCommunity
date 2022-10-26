package com.java110.dto.marketLog;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 营销记录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MarketLogDto extends PageDto implements Serializable {

    private String personName;
private String sendContent;
private String sendWay;
private String openId;
private String logId;
private String communityName;
private String remark;
private String ruleId;
private String communityId;
private String personTel;
private String businessType;


    private Date createTime;

    private String statusCd = "0";


    public String getPersonName() {
        return personName;
    }
public void setPersonName(String personName) {
        this.personName = personName;
    }
public String getSendContent() {
        return sendContent;
    }
public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }
public String getSendWay() {
        return sendWay;
    }
public void setSendWay(String sendWay) {
        this.sendWay = sendWay;
    }
public String getOpenId() {
        return openId;
    }
public void setOpenId(String openId) {
        this.openId = openId;
    }
public String getLogId() {
        return logId;
    }
public void setLogId(String logId) {
        this.logId = logId;
    }
public String getCommunityName() {
        return communityName;
    }
public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getRuleId() {
        return ruleId;
    }
public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getPersonTel() {
        return personTel;
    }
public void setPersonTel(String personTel) {
        this.personTel = personTel;
    }
public String getBusinessType() {
        return businessType;
    }
public void setBusinessType(String businessType) {
        this.businessType = businessType;
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
