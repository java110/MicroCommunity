package com.java110.po.store;

import java.io.Serializable;

/**
 * @ClassName StoreCerdentialPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 22:14
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class StoreCerdentialPo implements Serializable {

    private String storeCerdentialsId;

    private String storeId;
    private String credentialsCd;
    private String value;
    private String validityPeriod;
    private String positivePhoto;
    private String negativePhoto;


    public String getStoreCerdentialsId() {
        return storeCerdentialsId;
    }

    public void setStoreCerdentialsId(String storeCerdentialsId) {
        this.storeCerdentialsId = storeCerdentialsId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCredentialsCd() {
        return credentialsCd;
    }

    public void setCredentialsCd(String credentialsCd) {
        this.credentialsCd = credentialsCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getPositivePhoto() {
        return positivePhoto;
    }

    public void setPositivePhoto(String positivePhoto) {
        this.positivePhoto = positivePhoto;
    }

    public String getNegativePhoto() {
        return negativePhoto;
    }

    public void setNegativePhoto(String negativePhoto) {
        this.negativePhoto = negativePhoto;
    }
}
