package com.java110.po.rentingConfig;

import java.io.Serializable;

public class RentingConfigPo implements Serializable {

    private String serviceTenantRate;
    private String propertySeparateRate;
    private String servicePrice;
    private String adminSeparateRate;
    private String serviceOwnerRate;
    private String rentingConfigId;
    private String statusCd = "0";
    private String rentingType;
    private String proxySeparateRate;
    private String rentingFormula;

    public String getServiceTenantRate() {
        return serviceTenantRate;
    }

    public void setServiceTenantRate(String serviceTenantRate) {
        this.serviceTenantRate = serviceTenantRate;
    }

    public String getPropertySeparateRate() {
        return propertySeparateRate;
    }

    public void setPropertySeparateRate(String propertySeparateRate) {
        this.propertySeparateRate = propertySeparateRate;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getAdminSeparateRate() {
        return adminSeparateRate;
    }

    public void setAdminSeparateRate(String adminSeparateRate) {
        this.adminSeparateRate = adminSeparateRate;
    }

    public String getServiceOwnerRate() {
        return serviceOwnerRate;
    }

    public void setServiceOwnerRate(String serviceOwnerRate) {
        this.serviceOwnerRate = serviceOwnerRate;
    }

    public String getRentingConfigId() {
        return rentingConfigId;
    }

    public void setRentingConfigId(String rentingConfigId) {
        this.rentingConfigId = rentingConfigId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getRentingType() {
        return rentingType;
    }

    public void setRentingType(String rentingType) {
        this.rentingType = rentingType;
    }

    public String getProxySeparateRate() {
        return proxySeparateRate;
    }

    public void setProxySeparateRate(String proxySeparateRate) {
        this.proxySeparateRate = proxySeparateRate;
    }

    public String getRentingFormula() {
        return rentingFormula;
    }

    public void setRentingFormula(String rentingFormula) {
        this.rentingFormula = rentingFormula;
    }


}
