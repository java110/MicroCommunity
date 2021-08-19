package com.java110.po.oaWorkflowXml;

import java.io.Serializable;
import java.util.Date;

public class OaWorkflowXmlPo implements Serializable {

    private String statusCd = "0";
private String xmlId;
private String storeId;
private String bpmnXml;
private String flowId;
private String svgXml;
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getXmlId() {
        return xmlId;
    }
public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getBpmnXml() {
        return bpmnXml;
    }
public void setBpmnXml(String bpmnXml) {
        this.bpmnXml = bpmnXml;
    }
public String getFlowId() {
        return flowId;
    }
public void setFlowId(String flowId) {
        this.flowId = flowId;
    }
public String getSvgXml() {
        return svgXml;
    }
public void setSvgXml(String svgXml) {
        this.svgXml = svgXml;
    }



}
