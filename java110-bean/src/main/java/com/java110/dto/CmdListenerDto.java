package com.java110.dto;

import java.io.Serializable;

public class CmdListenerDto implements Serializable {
    private String beanName;

    private String serviceCode;

    public CmdListenerDto() {
    }

    public CmdListenerDto(String beanName, String serviceCode) {
        this.beanName = beanName;
        this.serviceCode = serviceCode;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
