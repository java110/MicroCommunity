package com.java110.dto.maintainance;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 保养设备数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MaintainancePlanMachineDto extends PageDto implements Serializable {

    private String mpmId;
    private String machineId;
    private String planId;
    private String[] planIds;
    private String communityId;
    private String machineName;

    private long machineCount;


    private Date createTime;

    private String statusCd = "0";


    public String getMpmId() {
        return mpmId;
    }

    public void setMpmId(String mpmId) {
        this.mpmId = mpmId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
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

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
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

    public long getMachineCount() {
        return machineCount;
    }

    public void setMachineCount(long machineCount) {
        this.machineCount = machineCount;
    }
}
