package com.java110.config.properties.code;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by wuxw on 2017/7/25.
 */
@Component
@ConfigurationProperties(prefix = "java110")
@PropertySource("classpath:java110.properties")
public class Java110Properties {

    private String mappingPath;

    private String wxAppId;

    private String wxAppSecret;

    private boolean autoReloadComponent = false;

    private String ftpServer;
    private int ftpPort;
    private String ftpUserName;
    private String ftpUserPassword;

    private String ftpPath;

    private String testSwitch;


    public String getMappingPath() {
        return mappingPath;
    }

    public void setMappingPath(String mappingPath) {
        this.mappingPath = mappingPath;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }

    public void setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret;
    }

    public boolean getAutoReloadComponent() {
        return autoReloadComponent;
    }

    public void setAutoReloadComponent(boolean autoReloadComponent) {
        this.autoReloadComponent = autoReloadComponent;
    }

    public String getFtpServer() {
        return ftpServer;
    }

    public void setFtpServer(String ftpServer) {
        this.ftpServer = ftpServer;
    }


    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpUserPassword() {
        return ftpUserPassword;
    }

    public void setFtpUserPassword(String ftpUserPassword) {
        this.ftpUserPassword = ftpUserPassword;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getTestSwitch() {
        //return testSwitch;
        return "0";
    }

    public void setTestSwitch(String testSwitch) {
        this.testSwitch = testSwitch;
    }
}
