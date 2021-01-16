package com.java110.dto.tempCarFeeConfig;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 临时车 停车费规则实体类
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class TempCarFeeRuleDto extends PageDto implements Serializable {

    private String ruleId;
    private String ruleName;
    private String beanName;
    private String remark;

    private Date createTime;

    private String statusCd = "0";

    private List<TempCarFeeRuleSpecDto> tempCarFeeRuleSpecs;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public List<TempCarFeeRuleSpecDto> getTempCarFeeRuleSpecs() {
        return tempCarFeeRuleSpecs;
    }

    public void setTempCarFeeRuleSpecs(List<TempCarFeeRuleSpecDto> tempCarFeeRuleSpecs) {
        this.tempCarFeeRuleSpecs = tempCarFeeRuleSpecs;
    }
}
