package com.java110.po.inspection;

import java.io.Serializable;

/**
 * @ClassName InspectionPlanStaffPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 19:07
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class InspectionPlanStaffPo implements Serializable {

    private String ipStaffId;
    private String inspectionPlanId;
    private String communityId;
    private String staffId;
    private String staffName;
    private String startTime;
    private String endTime;
    private String statusCd = "0";

    public String getIpStaffId() {
        return ipStaffId;
    }

    public void setIpStaffId(String ipStaffId) {
        this.ipStaffId = ipStaffId;
    }

    public String getInspectionPlanId() {
        return inspectionPlanId;
    }

    public void setInspectionPlanId(String inspectionPlanId) {
        this.inspectionPlanId = inspectionPlanId;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
