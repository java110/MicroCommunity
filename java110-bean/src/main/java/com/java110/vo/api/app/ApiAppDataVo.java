package com.java110.vo.api.app;

import java.io.Serializable;
import java.util.Date;

public class ApiAppDataVo implements Serializable {

    private String appId;
    private String name;
    private String securityCode;
    private String whileListIp;
    private String blackListIp;
    private String remark;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getWhileListIp() {
        return whileListIp;
    }

    public void setWhileListIp(String whileListIp) {
        this.whileListIp = whileListIp;
    }

    public String getBlackListIp() {
        return blackListIp;
    }

    public void setBlackListIp(String blackListIp) {
        this.blackListIp = blackListIp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
