package com.java110.dto.payment;

import java.io.Serializable;

public class NotifyPaymentOrderDto implements Serializable{

    private String appId;

    private String param;

    private String communityId;

    public NotifyPaymentOrderDto(String appId, String param,String communityId) {
        this.appId = appId;
        this.param = param;
        this.communityId = communityId;
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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
