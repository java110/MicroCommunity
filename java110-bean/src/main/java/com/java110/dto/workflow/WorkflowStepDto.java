package com.java110.dto.workflow;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 工作流节点数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WorkflowStepDto extends PageDto implements Serializable {

    public static final String TYPE_NORMAL = "2"; //正常流程
    public static final String TYPE_COUNTERSIGN = "1"; //会签

    private String stepId;
    private String type;
    private String communityId;
    private String storeId;
    private String flowId;
    private String seq;

    private List<WorkflowStepStaffDto> workflowStepStaffs;


    private Date createTime;

    private String statusCd = "0";


    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
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

    public List<WorkflowStepStaffDto> getWorkflowStepStaffs() {
        return workflowStepStaffs;
    }

    public void setWorkflowStepStaffs(List<WorkflowStepStaffDto> workflowStepStaffs) {
        this.workflowStepStaffs = workflowStepStaffs;
    }
}
