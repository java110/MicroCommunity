package com.java110.po.user;

import java.io.Serializable;

/**
 * @ClassName UserCredentialPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 22:46
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class UserCredentialPo implements Serializable {

    private String id;
    private String credentialsId;
    private String userId;
    private String credentialsCd;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
