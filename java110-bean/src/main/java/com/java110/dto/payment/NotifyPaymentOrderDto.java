package com.java110.dto.payment;

import java.io.Serializable;

public class NotifyPaymentOrderDto implements Serializable{

    private String appId;

    private String param;

    public NotifyPaymentOrderDto(String appId, String param) {
        this.appId = appId;
        this.param = param;
    }

    public NotifyPaymentOrderDto() {
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
}
