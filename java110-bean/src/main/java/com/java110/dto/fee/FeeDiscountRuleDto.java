package com.java110.dto.fee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 费用折扣规则数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeDiscountRuleDto extends PageDto implements Serializable {

    private String ruleName;
    private String remark;
    private String ruleId;
    private String beanImpl;
    private String discountType;
    private String discountSmallType;

    private List<FeeDiscountRuleSpecDto> feeDiscountRuleSpecs;


    private Date createTime;

    private String statusCd = "0";


    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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

    public String getBeanImpl() {
        return beanImpl;
    }

    public void setBeanImpl(String beanImpl) {
        this.beanImpl = beanImpl;
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

    public List<FeeDiscountRuleSpecDto> getFeeDiscountRuleSpecs() {
        return feeDiscountRuleSpecs;
    }

    public void setFeeDiscountRuleSpecs(List<FeeDiscountRuleSpecDto> feeDiscountRuleSpecs) {
        this.feeDiscountRuleSpecs = feeDiscountRuleSpecs;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountSmallType() {
        return discountSmallType;
    }

    public void setDiscountSmallType(String discountSmallType) {
        this.discountSmallType = discountSmallType;
    }
}
