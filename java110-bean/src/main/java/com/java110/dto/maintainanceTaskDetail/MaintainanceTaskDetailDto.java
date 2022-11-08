package com.java110.dto.maintainanceTaskDetail;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 保养明细数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MaintainanceTaskDetailDto extends PageDto implements Serializable {

    private String sendFlag;
private String pointEndTime;
private String inspectionTime;
private String sortNumber;
private String machineName;
private String actUserName;
private String taskDetailId;
private String machineId;
private String pointStartTime;
private String state;
private String communityId;
private String actUserId;
private String taskId;


    private Date createTime;

    private String statusCd = "0";


    public String getSendFlag() {
        return sendFlag;
    }
public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag;
    }
public String getPointEndTime() {
        return pointEndTime;
    }
public void setPointEndTime(String pointEndTime) {
        this.pointEndTime = pointEndTime;
    }
public String getInspectionTime() {
        return inspectionTime;
    }
public void setInspectionTime(String inspectionTime) {
        this.inspectionTime = inspectionTime;
    }
public String getSortNumber() {
        return sortNumber;
    }
public void setSortNumber(String sortNumber) {
        this.sortNumber = sortNumber;
    }
public String getMachineName() {
        return machineName;
    }
public void setMachineName(String machineName) {
        this.machineName = machineName;
    }
public String getActUserName() {
        return actUserName;
    }
public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }
public String getTaskDetailId() {
        return taskDetailId;
    }
public void setTaskDetailId(String taskDetailId) {
        this.taskDetailId = taskDetailId;
    }
public String getMachineId() {
        return machineId;
    }
public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
public String getPointStartTime() {
        return pointStartTime;
    }
public void setPointStartTime(String pointStartTime) {
        this.pointStartTime = pointStartTime;
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
public String getActUserId() {
        return actUserId;
    }
public void setActUserId(String actUserId) {
        this.actUserId = actUserId;
    }
public String getTaskId() {
        return taskId;
    }
public void setTaskId(String taskId) {
        this.taskId = taskId;
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
