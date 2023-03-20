package com.java110.dto.meterWater;

import java.io.Serializable;

public class NotifyMeterWaterOrderDto implements Serializable{

    private String appId;

    private String implBean;

    private String param;

    public NotifyMeterWaterOrderDto(String appId, String param,String implBean) {
        this.appId = appId;
        this.param = param;
        this.implBean = implBean;
    }

    public NotifyMeterWaterOrderDto() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getImplBean() {
        return implBean;
    }

    public void setImplBean(String implBean) {
        this.implBean = implBean;
    }
}
