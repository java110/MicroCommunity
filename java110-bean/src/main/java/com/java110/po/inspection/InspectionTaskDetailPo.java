package com.java110.po.inspection;

import java.io.Serializable;

/**
 * @ClassName InspectionTaskDetailPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 21:50
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class InspectionTaskDetailPo implements Serializable {

    private String taskDetailId;

    private String communityId;
    private String taskId;
    private String inspectionId;
    private String inspectionName;
    private String state;

    private String patrolType;
    private String description;

    public String getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(String taskDetailId) {
        this.taskDetailId = taskDetailId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPatrolType() {
        return patrolType;
    }

    public void setPatrolType(String patrolType) {
        this.patrolType = patrolType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
