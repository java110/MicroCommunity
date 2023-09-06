package com.java110.dto.resource;

import com.java110.dto.PageDto;
import com.java110.dto.oaWorkflow.OaWorkflowDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 物品流程数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ResourceAuditFlowDto extends OaWorkflowDto implements Serializable {


    public static final String AUDIT_TYPE_PURCHASE_APPLY="10001";//采购
    public static final String AUDIT_TYPE_RESOURCE_OUT="10002";//领用
    public static final String AUDIT_TYPE_ALLOCATION="10003";//调拨流程

    private String rafId;
    private String remark;
    private String storeId;
    private String flowId;
    private String flowName;
    private String auditType;

    private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getRafId() {
        return rafId;
    }

    public void setRafId(String rafId) {
        this.rafId = rafId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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


    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
