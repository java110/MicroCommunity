package com.java110.dto.integral;

import java.io.Serializable;

public class DeductionIntegralDto implements Serializable {

    private int integral;

    private double money;

    private String link;

    private String communityId;

    private String remark;

    public DeductionIntegralDto(int integral, double money) {
        this.integral = integral;
        this.money = money;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
