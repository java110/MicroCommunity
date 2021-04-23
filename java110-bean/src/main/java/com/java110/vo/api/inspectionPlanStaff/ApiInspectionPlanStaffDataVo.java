package com.java110.vo.api.inspectionPlanStaff;

import java.io.Serializable;
import java.util.Date;

public class ApiInspectionPlanStaffDataVo implements Serializable {

    private String operate;
private String createTime;
private String ipStaffId;
private String staffName;
private String startTime;
private String statusCd;
private String inspectionPlanId;
private String endTime;
private String bId;
private String communityId;
private String staffId;
public String getOperate() {
        return operate;
    }
public void setOperate(String operate) {
        this.operate = operate;
    }
public String getCreateTime() {
        return createTime;
    }
public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
public String getIpStaffId() {
        return ipStaffId;
    }
public void setIpStaffId(String ipStaffId) {
        this.ipStaffId = ipStaffId;
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
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getInspectionPlanId() {
        return inspectionPlanId;
    }
public void setInspectionPlanId(String inspectionPlanId) {
        this.inspectionPlanId = inspectionPlanId;
    }
public String getEndTime() {
        return endTime;
    }
public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
public String getBId() {
        return bId;
    }
public void setBId(String bId) {
        this.bId = bId;
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
