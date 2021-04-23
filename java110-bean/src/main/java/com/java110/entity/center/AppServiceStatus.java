package com.java110.entity.center;

import java.io.Serializable;

/**
 * 服务状态对象
 * Created by wuxw on 2018/4/14.
 */
public class AppServiceStatus  implements Serializable{

    private AppService appService;
    private String statusCd;

    public AppServiceStatus() {
    }

    public AppServiceStatus(AppService appService, String statusCd) {
        this.appService = appService;
        this.statusCd = statusCd;
    }

    public AppService getAppService() {
        return appService;
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
