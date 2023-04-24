package com.java110.dto.rentingPool;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 房屋出租配置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RentingConfigDto extends PageDto implements Serializable {

    public static final String RENTING_FORMULA_RATE = "2002";

    private String serviceTenantRate;
    private String propertySeparateRate;
    private String servicePrice;
    private String adminSeparateRate;
    private String serviceOwnerRate;
    private String rentingConfigId;
    private String rentingType;
    private String rentingTypeName;
    private String proxySeparateRate;
    private String rentingFormula;
    private String rentingFormulaName;


    private Date createTime;

    private String statusCd = "0";


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


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }


    public String getRentingTypeName() {
        return rentingTypeName;
    }

    public void setRentingTypeName(String rentingTypeName) {
        this.rentingTypeName = rentingTypeName;
    }

    public String getRentingFormulaName() {
        return rentingFormulaName;
    }

    public void setRentingFormulaName(String rentingFormulaName) {
        this.rentingFormulaName = rentingFormulaName;
    }
}
