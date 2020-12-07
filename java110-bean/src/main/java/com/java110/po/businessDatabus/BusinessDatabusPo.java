package com.java110.po.businessDatabus;

import java.io.Serializable;

public class BusinessDatabusPo implements Serializable {

    private String businessTypeCd;
    private String databusId;
    private String beanName;
    private String statusCd = "0";
    private String seq;

    public String getBusinessTypeCd() {
        return businessTypeCd;
    }

    public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }

    public String getDatabusId() {
        return databusId;
    }

    public void setDatabusId(String databusId) {
        this.databusId = databusId;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }


}
