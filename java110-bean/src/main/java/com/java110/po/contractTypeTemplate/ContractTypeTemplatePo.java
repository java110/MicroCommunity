package com.java110.po.contractTypeTemplate;

import java.io.Serializable;

public class ContractTypeTemplatePo implements Serializable {

    private String context;
    private String statusCd = "0";
    private String storeId;
    private String contractTypeId;
    private String templateId;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getContractTypeId() {
        return contractTypeId;
    }

    public void setContractTypeId(String contractTypeId) {
        this.contractTypeId = contractTypeId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
