package com.java110.dto.inspectionPoint;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 巡检点数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InspectionDto extends PageDto implements Serializable {

    private String inspectionId;
private String machineId;
private String remark;
private String inspectionName;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getInspectionId() {
        return inspectionId;
    }
public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }
public String getMachineId() {
        return machineId;
    }
public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getInspectionName() {
        return inspectionName;
    }
public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
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
