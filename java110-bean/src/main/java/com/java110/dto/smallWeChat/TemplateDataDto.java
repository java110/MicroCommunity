package com.java110.dto.smallWeChat;

import java.io.Serializable;

public class TemplateDataDto implements Serializable {

    private String appId;

    private String templateId;

    private String accessToken;

    public TemplateDataDto() {
    }

    public TemplateDataDto(String templateId, String accessToken,String appId) {
        this.templateId = templateId;
        this.accessToken = accessToken;
        this.appId = appId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
