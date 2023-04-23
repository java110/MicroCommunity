package com.java110.dto.printerRule;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 云打印规则费用数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PrinterRuleFeeDto extends PageDto implements Serializable {

    private String prfId;
    private String ruleId;
    private String communityId;
    private String feeId;

    private String feeConfigName;


    private Date createTime;

    private String statusCd = "0";


    public String getPrfId() {
        return prfId;
    }

    public void setPrfId(String prfId) {
        this.prfId = prfId;
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

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
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

    public String getFeeConfigName() {
        return feeConfigName;
    }

    public void setFeeConfigName(String feeConfigName) {
        this.feeConfigName = feeConfigName;
    }
}
