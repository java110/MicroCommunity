package com.java110.po.machine;

import java.io.Serializable;

/**
 * @ClassName MachineTranslatePo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 9:19
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class MachineTranslatePo implements Serializable {
    private String machineTranslateId;
    private String machineId;
    private String machineCode;
    private String typeCd;
    private String objId;
    private String communityId;
    private String objName;
    private String state;
    private String updateTime;
    private String remark;
    private String statusCd = "0";


    public String getMachineTranslateId() {
        return machineTranslateId;
    }

    public void setMachineTranslateId(String machineTranslateId) {
        this.machineTranslateId = machineTranslateId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
