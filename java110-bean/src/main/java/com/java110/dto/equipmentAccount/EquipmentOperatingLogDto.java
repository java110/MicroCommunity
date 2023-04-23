package com.java110.dto.equipmentAccount;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 设备操作记录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class EquipmentOperatingLogDto extends PageDto implements Serializable {

    private String operatingCode;
    private String machineId;
    private String operatingId;
    private String remark;
    private String communityId;
    private String operatingDescriptor;
    private String userName;
    private String userId;
    private String useTel;


    private Date createTime;

    private String statusCd = "0";


    public String getOperatingCode() {
        return operatingCode;
    }

    public void setOperatingCode(String operatingCode) {
        this.operatingCode = operatingCode;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getOperatingId() {
        return operatingId;
    }

    public void setOperatingId(String operatingId) {
        this.operatingId = operatingId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getOperatingDescriptor() {
        return operatingDescriptor;
    }

    public void setOperatingDescriptor(String operatingDescriptor) {
        this.operatingDescriptor = operatingDescriptor;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUseTel() {
        return useTel;
    }

    public void setUseTel(String useTel) {
        this.useTel = useTel;
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
