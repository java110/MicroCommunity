package com.java110.dto.wechatSmsTemplate;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 微信消息模板数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WechatSmsTemplateDto extends PageDto implements Serializable {

    private String templateType;
private String wechatId;
private String smsTemplateId;
private String templateId;
private String communityId;
private String remarks;


    private Date createTime;

    private String statusCd = "0";


    public String getTemplateType() {
        return templateType;
    }
public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
public String getWechatId() {
        return wechatId;
    }
public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }
public String getSmsTemplateId() {
        return smsTemplateId;
    }
public void setSmsTemplateId(String smsTemplateId) {
        this.smsTemplateId = smsTemplateId;
    }
public String getTemplateId() {
        return templateId;
    }
public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
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
