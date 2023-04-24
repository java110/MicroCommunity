package com.java110.dto.inspection;

import java.io.Serializable;

/**
 * 员工巡检情况统计
 * <p>
 * add by wuxw 2022-11-15
 */
public class InspectionStaffDto implements Serializable {
    private String staffId;
    private String staffName;
    private String finishCount;
    private String waitCount;
    private String communityId;
    private String queryTime;
    private String state;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(String finishCount) {
        this.finishCount = finishCount;
    }

    public String getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(String waitCount) {
        this.waitCount = waitCount;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
