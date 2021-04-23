package com.java110.po.userLogin;

import java.io.Serializable;
import java.util.Date;

public class UserLoginPo implements Serializable {

    private String password;
private String loginId;
private String loginTime;
private String source;
private String userName;
private String userId;
private String token;
public String getPassword() {
        return password;
    }
public void setPassword(String password) {
        this.password = password;
    }
public String getLoginId() {
        return loginId;
    }
public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
public String getLoginTime() {
        return loginTime;
    }
public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
public String getSource() {
        return source;
    }
public void setSource(String source) {
        this.source = source;
    }
public String getUserName() {
        return userName;
    }
public void setUserName(String userName) {
        this.userName = userName;
    }
public String getUserId() {
        return userId;
    }
public void setUserId(String userId) {
        this.userId = userId;
    }
public String getToken() {
        return token;
    }
public void setToken(String token) {
        this.token = token;
    }



}
