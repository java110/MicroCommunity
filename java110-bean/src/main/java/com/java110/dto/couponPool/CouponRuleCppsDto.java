package com.java110.dto.couponPool;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 规则优惠券数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CouponRuleCppsDto extends PageDto implements Serializable {

    public static final String FREQUENCY_ONCE = "100301"; // 赠送一次
    public static final String FREQUENCY_MONTH = "1003012"; // 每月赠送一次

    private String quantity;
    private String crcId;
    private String cppId;
    private String couponName;
    private String ruleId;
    private String ruleName;
    private String toTypeName;
    private String communityId;
    private String[] ruleIds;
    private String remark;
    private String giftFrequency;
    private String giftFrequencyName;


    private Date createTime;

    private String statusCd = "0";


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCrcId() {
        return crcId;
    }

    public void setCrcId(String crcId) {
        this.crcId = crcId;
    }

    public String getCppId() {
        return cppId;
    }

    public void setCppId(String cppId) {
        this.cppId = cppId;
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

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String[] getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(String[] ruleIds) {
        this.ruleIds = ruleIds;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getToTypeName() {
        return toTypeName;
    }

    public void setToTypeName(String toTypeName) {
        this.toTypeName = toTypeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGiftFrequency() {
        return giftFrequency;
    }

    public void setGiftFrequency(String giftFrequency) {
        this.giftFrequency = giftFrequency;
    }

    public String getGiftFrequencyName() {
        return giftFrequencyName;
    }

    public void setGiftFrequencyName(String giftFrequencyName) {
        this.giftFrequencyName = giftFrequencyName;
    }
}
