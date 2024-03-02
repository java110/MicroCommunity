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

    //todo 1: 打折  2:减免  3:滞纳金  4:空置房打折  5:空置房减免

    public static final String DISCOUNT_SMALL_TYPE_DISCOUNT = "1"; //1: 打折
    public static final String DISCOUNT_SMALL_TYPE_DEDUCTION = "2"; //2:减免
    public static final String DISCOUNT_SMALL_TYPE_LATE = "3"; //3:滞纳金
    public static final String DISCOUNT_SMALL_TYPE_APPLY_DISCOUNT = "4"; //4:空置房打折
    public static final String DISCOUNT_SMALL_TYPE_APPLY_DEDUCTION = "5"; //5:空置房减免

    public static final String DISCOUNT_SMALL_TYPE_GIFT = "6"; //6:赠送


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
