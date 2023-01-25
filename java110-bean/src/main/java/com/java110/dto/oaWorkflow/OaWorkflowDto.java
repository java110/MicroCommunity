package com.java110.dto.oaWorkflow;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description OA工作流数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OaWorkflowDto extends PageDto implements Serializable {

    public static final String STATE_WAIT = "W";//待部署
    public static final String STATE_COMPLAINT = "C";//部署完成

    public static final String FLOW_TYPE_ITEM_RELEASE="2002";//物品放行
    public static final String FLOW_TYPE_VISIT="3003";// 访客审核
    public static final String FLOW_TYPE_PUBLIC="1001";//物品放行

    public static final String FLOW_TYPE_OWNER_SETTLED="4004";// 业主入驻房屋流程

    private String userId;
    private String describle;
    private String modelId;
    private String flowKey;
    private String storeId;
    private String flowId;
    private String flowName;
    private String flowType;
    private String processDefinitionKey;
    private String state;
    private String curFormId;
    private long undoCount;

    private Date createTime;

    private String statusCd = "0";

    private String[] flowIds;


    public String getDescrible() {
        return describle;
    }

    public void setDescrible(String describle) {
        this.describle = describle;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getFlowKey() {
        return flowKey;
    }

    public void setFlowKey(String flowKey) {
        this.flowKey = flowKey;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCurFormId() {
        return curFormId;
    }

    public void setCurFormId(String curFormId) {
        this.curFormId = curFormId;
    }

    public long getUndoCount() {
        return undoCount;
    }

    public void setUndoCount(long undoCount) {
        this.undoCount = undoCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getFlowIds() {
        return flowIds;
    }

    public void setFlowIds(String[] flowIds) {
        this.flowIds = flowIds;
    }
}
