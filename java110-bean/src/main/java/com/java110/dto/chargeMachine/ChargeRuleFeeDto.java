package com.java110.dto.chargeMachine;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 充电价格数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ChargeRuleFeeDto extends PageDto implements Serializable {

    private String maxEnergyPrice;
    private String durationPrice;
    private String crfId;
    private String remark;
    private String ruleId;
    private String minEnergyPrice;
    private String communityId;

    private String energy;


    private Date createTime;

    private String statusCd = "0";


    public String getMaxEnergyPrice() {
        return maxEnergyPrice;
    }

    public void setMaxEnergyPrice(String maxEnergyPrice) {
        this.maxEnergyPrice = maxEnergyPrice;
    }

    public String getDurationPrice() {
        return durationPrice;
    }

    public void setDurationPrice(String durationPrice) {
        this.durationPrice = durationPrice;
    }

    public String getCrfId() {
        return crfId;
    }

    public void setCrfId(String crfId) {
        this.crfId = crfId;
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

    public String getMinEnergyPrice() {
        return minEnergyPrice;
    }

    public void setMinEnergyPrice(String minEnergyPrice) {
        this.minEnergyPrice = minEnergyPrice;
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

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }
}
