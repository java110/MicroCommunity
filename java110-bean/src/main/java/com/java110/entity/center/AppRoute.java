package com.java110.entity.center;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private String invokeModel;



    //掩码
    private String securityCode;
    //白名单
    private List<String> whileListIp = new ArrayList<String>();

    //黑名单
    private List<String> backListIp = new ArrayList<String>();

    //服务
    //private List<AppServiceStatus> appServices = new ArrayList<AppServiceStatus>();
    private AppService appService;

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

    public String getInvokeModel() {
        return invokeModel;
    }

    public void setInvokeModel(String invokeModel) {
        this.invokeModel = invokeModel;
    }

    /*public List<AppServiceStatus> getAppServices() {
        return appServices;
    }

    public void addAppServices(AppServiceStatus appServiceStatus) {
        this.appServices.add(appServiceStatus);
    }*/

    public AppService getAppService() {
        return appService;
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
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
    public AppRoute builder(Map appInfo){
        String []listIps = null;
        this.setAppId(appInfo.get("app_id").toString());
        this.setLimitTimes(appInfo.get("invoke_limit_times") == null ? -1 : Integer.parseInt(appInfo.get("invoke_limit_times").toString()));
        this.setName(appInfo.get("name").toString());
        this.setOrderTypeCd(appInfo.get("order_type_cd").toString());
        this.setSecurityCode(appInfo.get("security_code").toString());
        this.setInvokeModel(appInfo.get("invoke_model").toString());
        if(appInfo.get("while_list_ip") != null && !"".equals(appInfo.get("while_list_ip"))){
            listIps = appInfo.get("while_list_ip").toString().split(";");
            for(String whileIp : listIps )
                this.addWhileListIp(whileIp);
        }
        if(appInfo.get("black_list_ip") != null && !"".equals(appInfo.get("black_list_ip"))){
            listIps = appInfo.get("black_list_ip").toString().split(";");
            for(String backIp : listIps )
                this.addBackListIp(backIp);
        }
        this.setStatusCd("0");
        this.setAppService(AppService.newInstance().builder(appInfo));
        return this;
    }



    public static AppRoute newInstance(){
        return new AppRoute();
    }


}
