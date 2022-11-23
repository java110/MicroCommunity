package com.java110.dto.couponRuleCpps;

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

    private String quantity;
    private String crcId;
    private String cppId;
    private String couponName;
    private String ruleId;
    private String communityId;


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
}
