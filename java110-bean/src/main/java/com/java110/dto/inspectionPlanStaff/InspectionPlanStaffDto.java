package com.java110.dto.inspectionPlanStaff;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 执行计划人数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InspectionPlanStaffDto extends PageDto implements Serializable {

    private String ipStaffId;
    private String staffName;
    private String startTime;
    private String inspectionPlanId;
    private String endTime;
    private String communityId;
    private String staffId;

    private Date createTime;

    private String statusCd = "0";


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
}
