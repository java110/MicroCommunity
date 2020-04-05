package com.java110.vo.api.inspectionTaskDetail;

import java.io.Serializable;
import java.util.Date;

public class ApiInspectionTaskDetailDataVo implements Serializable {

    private String inspectionId;
private String operate;
private String statusCd;
private String inspectionName;
private String state;
private String communityId;
private String bId;
private String taskId;
private String taskDetailId;
public String getInspectionId() {
        return inspectionId;
    }
public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }
public String getOperate() {
        return operate;
    }
public void setOperate(String operate) {
        this.operate = operate;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
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
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getBId() {
        return bId;
    }
public void setBId(String bId) {
        this.bId = bId;
    }
public String getTaskId() {
        return taskId;
    }
public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
public String getTaskDetailId() {
        return taskDetailId;
    }
public void setTaskDetailId(String taskDetailId) {
        this.taskDetailId = taskDetailId;
    }



}
