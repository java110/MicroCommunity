package com.java110.po.workflow;

import java.io.Serializable;

public class WorkflowPo implements Serializable {

    private String skipLevel;
    private String describle;
    private String communityId;
    private String storeId;
    private String flowId;
    private String flowName;
    private String flowType;
    private String processDefinitionKey;
    private String statusCd = "0";
    private String startNodeFinish ="N";

    public String getSkipLevel() {
        return skipLevel;
    }

    public void setSkipLevel(String skipLevel) {
        this.skipLevel = skipLevel;
    }

    public String getDescrible() {
        return describle;
    }

    public void setDescrible(String describle) {
        this.describle = describle;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getStartNodeFinish() {
        return startNodeFinish;
    }

    public void setStartNodeFinish(String startNodeFinish) {
        this.startNodeFinish = startNodeFinish;
    }
}
