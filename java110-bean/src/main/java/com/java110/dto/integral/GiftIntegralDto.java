package com.java110.dto.integral;

import java.io.Serializable;

public class GiftIntegralDto implements Serializable {

    public GiftIntegralDto() {
    }

    public GiftIntegralDto(int integral, double money) {
        this.integral = integral;
        this.money = money;
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
}
