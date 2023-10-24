package com.java110.dto.payFeeRuleBill;

import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 费用账单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PayFeeRuleBillDto extends FeeDto implements Serializable {

    private String billName;
    private String configId;
    private String billId;
    private String curYearMonth;
    private String remark;
    private String ruleId;
    private String communityId;
    private String batchId;
    private String feeId;

    private String monthCycle;


    private Date createTime;

    private String statusCd = "0";


    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCurYearMonth() {
        return curYearMonth;
    }

    public void setCurYearMonth(String curYearMonth) {
        this.curYearMonth = curYearMonth;
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

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
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

    public String getMonthCycle() {
        return monthCycle;
    }

    public void setMonthCycle(String monthCycle) {
        this.monthCycle = monthCycle;
    }
}
