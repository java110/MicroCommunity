package com.java110.dto.fee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 费用折扣数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeDiscountDto extends PageDto implements Serializable {

    //类型 1001 优惠  2002 违约  3003 优惠(需要申请)
    public static final String DISCOUNT_TYPE_D = "1001"; //优惠
    public static final String DISCOUNT_TYPE_V = "2002"; //违约
    public static final String DISCOUNT_TYPE_DV = "3003"; //优惠(需要申请)

    private String discountName;
    private String discountDesc;
    private String discountType;
    private String discountTypeName;
    private String discountId;
    private String communityId;
    private String ruleId;
    private String ruleName;
    private String beanImpl;
    private String feeId;
    private double cycles;

    private List<FeeDiscountSpecDto> feeDiscountSpecs;

    private Date createTime;

    private String statusCd = "0";

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getDiscountDesc() {
        return discountDesc;
    }

    public void setDiscountDesc(String discountDesc) {
        this.discountDesc = discountDesc;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
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

    public List<FeeDiscountSpecDto> getFeeDiscountSpecs() {
        return feeDiscountSpecs;
    }

    public void setFeeDiscountSpecs(List<FeeDiscountSpecDto> feeDiscountSpecs) {
        this.feeDiscountSpecs = feeDiscountSpecs;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getBeanImpl() {
        return beanImpl;
    }

    public void setBeanImpl(String beanImpl) {
        this.beanImpl = beanImpl;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public double getCycles() {
        return cycles;
    }

    public void setCycles(double cycles) {
        this.cycles = cycles;
    }

    public String getDiscountTypeName() {
        return discountTypeName;
    }

    public void setDiscountTypeName(String discountTypeName) {
        this.discountTypeName = discountTypeName;
    }
}
