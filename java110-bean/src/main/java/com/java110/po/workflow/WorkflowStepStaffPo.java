package com.java110.po.workflow;

import java.io.Serializable;
import java.util.Date;

public class WorkflowStepStaffPo implements Serializable {

    private String wssId;
private String stepId;
private String staffName;
private String communityId;
private String staffId;
public String getWssId() {
        return wssId;
    }
public void setWssId(String wssId) {
        this.wssId = wssId;
    }
public String getStepId() {
        return stepId;
    }
public void setStepId(String stepId) {
        this.stepId = stepId;
    }
public String getStaffName() {
        return staffName;
    }
public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getStaffId() {
        return staffId;
    }
public void setStaffId(String staffId) {
        this.staffId = staffId;
    }



}
