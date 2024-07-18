package com.java110.dto.integral;

import java.io.Serializable;

public class GiftIntegralDto implements Serializable {

    public GiftIntegralDto() {
    }

    public GiftIntegralDto(int integral, double money,String communityId) {
        this.integral = integral;
        this.money = money;
        this.communityId = communityId;
    }

    public GiftIntegralDto(int integral, double money,String communityId,
                           String ruleId,String ruleName,String configId,String configName,String platformMchId,String platformMchName
    ) {
        this.integral = integral;
        this.money = money;
        this.communityId = communityId;
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.configId = configId;
        this.configName = configName;
        this.platformMchId = platformMchId;
        this.platformMchName = platformMchName;
    }

    private int integral;

    private double money;

    private String platformMchId;

    private String platformMchName;

    private String appId;

    private String mchId;

    private String mchKey;

    private String certPath;

    private String userId;

    private String link;

    private String remark;

    private String orderId;

    private String communityId;

    private String configId;

    private String configName;

    private String ruleId;

    private String ruleName;




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

    public String getPlatformMchId() {
        return platformMchId;
    }

    public void setPlatformMchId(String platformMchId) {
        this.platformMchId = platformMchId;
    }

    public String getPlatformMchName() {
        return platformMchName;
    }

    public void setPlatformMchName(String platformMchName) {
        this.platformMchName = platformMchName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

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
}
