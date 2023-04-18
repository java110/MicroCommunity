package com.java110.dto.integral;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 积分规则费用数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class IntegralRuleFeeDto extends PageDto implements Serializable {

    private String payMonth;
    private String irfId;
    private String feeConfigName;
    private String payStartTime;
    private String payEndTime;
    private String ruleId;
    private String communityId;
    private String feeConfigId;

    private String curTime;
    private String cycle;


    private Date createTime;

    private String statusCd = "0";


    public String getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(String payMonth) {
        this.payMonth = payMonth;
    }

    public String getIrfId() {
        return irfId;
    }

    public void setIrfId(String irfId) {
        this.irfId = irfId;
    }

    public String getFeeConfigName() {
        return feeConfigName;
    }

    public void setFeeConfigName(String feeConfigName) {
        this.feeConfigName = feeConfigName;
    }

    public String getPayStartTime() {
        return payStartTime;
    }

    public void setPayStartTime(String payStartTime) {
        this.payStartTime = payStartTime;
    }

    public String getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(String payEndTime) {
        this.payEndTime = payEndTime;
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

    public String getFeeConfigId() {
        return feeConfigId;
    }

    public void setFeeConfigId(String feeConfigId) {
        this.feeConfigId = feeConfigId;
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

    public String getCurTime() {
        return curTime;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }
}
