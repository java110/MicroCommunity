package com.java110.dto.data;

import com.java110.dto.system.Business;

import java.io.Serializable;
import java.util.List;

/**
 * java110 队里数据封装
 */
public class DatabusQueueDataDto implements Serializable {

    public DatabusQueueDataDto() {
    }

    public DatabusQueueDataDto(String beanName, Business business, List<Business> businesses) {
        this.beanName = beanName;
        this.business = business;
        this.businesses = businesses;
    }

    private String dataId;

    private String beanName;

    private Business business;
    private List<Business> businesses;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
