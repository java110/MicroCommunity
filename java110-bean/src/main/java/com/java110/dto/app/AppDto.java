package com.java110.dto.app;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 应用数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AppDto extends PageDto implements Serializable {

    //web端APP_ID
    public static final String WEB_APP_ID = "8000418004";
    public static final String WECHAT_OWNER_APP_ID = "992020061452450002"; //业主公众号
    public static final String WECHAT_MINA_OWNER_APP_ID = "992019111758490006";
    public static final String WECHAT_MALL_APP_ID = "992021030901240071";
    public static final String OWNER_WECHAT_PAY = "992020011134400001"; // 微信支付
    public static final String OWNER_APP_APP_ID = "992020061440050003"; // app微信支付
    public static final String JOB_APP_ID = "992021061746360004"; // 定时任务应用


    private String appId;
    private String[] appIds;
    private String blackListIp;
    private String name;
    private String securityCode;
    private String remark;
    private String userId;
    private String whileListIp;


    private Date createTime;

    private String statusCd = "0";


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getBlackListIp() {
        return blackListIp;
    }

    public void setBlackListIp(String blackListIp) {
        this.blackListIp = blackListIp;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWhileListIp() {
        return whileListIp;
    }

    public void setWhileListIp(String whileListIp) {
        this.whileListIp = whileListIp;
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

    public String[] getAppIds() {
        return appIds;
    }

    public void setAppIds(String[] appIds) {
        this.appIds = appIds;
    }
}
