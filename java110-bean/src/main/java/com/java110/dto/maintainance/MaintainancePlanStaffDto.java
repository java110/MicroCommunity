package com.java110.dto.maintainance;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 保养计划员工数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MaintainancePlanStaffDto extends PageDto implements Serializable {

    private String staffName;
    private String planId;
    private String[] planIds;
    private String communityId;
    private String staffId;
    private String mpsId;
    private long staffCount;


    private Date createTime;

    private String statusCd = "0";


    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
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

    public String getMpsId() {
        return mpsId;
    }

    public void setMpsId(String mpsId) {
        this.mpsId = mpsId;
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

    public String[] getPlanIds() {
        return planIds;
    }

    public void setPlanIds(String[] planIds) {
        this.planIds = planIds;
    }

    public long getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(long staffCount) {
        this.staffCount = staffCount;
    }
}
