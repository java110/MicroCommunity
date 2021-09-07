package com.java110.dto.hcGovTranslate;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 社区政务同步数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class HcGovTranslateDto extends PageDto implements Serializable {

    private String tranId;
private String code;
private String serviceCode;
private String sign;
private String updateTime;
private String remark;
private String reqTime;
private String sendCount;
private String extCommunityId;
private String objId;
private String state;
private String communityId;
private String govTopic;


    private Date createTime;

    private String statusCd = "0";


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
