package com.java110.entity.center;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 路由处理
 * AppId 和 服务之间的关系
 * Created by wuxw on 2018/4/14.
 */
public class AppRoute implements Serializable{

    private String appId;

    // 应用名称
    private String name;

    private String orderTypeCd;

    //一分钟，调用限制次数
    private int limitTimes;

    //掩码
    private String securityCode;
    //白名单
    private List<String> whileListIp = new ArrayList<String>();

    //黑名单
    private List<String> backListIp = new ArrayList<String>();

    //服务
    private List<AppServiceStatus> appServices = new ArrayList<AppServiceStatus>();

    private String remark;

    //0在用，1失效，2 表示下线（当组件调用服务超过限制时自动下线）
    private String statusCd;


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

    public String getOrderTypeCd() {
        return orderTypeCd;
    }

    public void setOrderTypeCd(String orderTypeCd) {
        this.orderTypeCd = orderTypeCd;
    }

    public int getLimitTimes() {
        return limitTimes;
    }

    public void setLimitTimes(int limitTimes) {
        this.limitTimes = limitTimes;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public List<String> getWhileListIp() {
        return whileListIp;
    }

    public void addWhileListIp(String whileIp) {
        this.whileListIp.add(whileIp);
    }

    public List<String> getBackListIp() {
        return backListIp;
    }

    public void addBackListIp(String backIp) {
        this.backListIp.add(backIp);
    }

    public List<AppServiceStatus> getAppServices() {
        return appServices;
    }

    public void addAppServices(AppServiceStatus appServiceStatus) {
        this.appServices.add(appServiceStatus);
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    /**
     * 构建数据
     * @return
     */
    public AppRoute builder(){
        return this;
    }





}
