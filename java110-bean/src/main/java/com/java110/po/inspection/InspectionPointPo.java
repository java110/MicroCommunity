package com.java110.po.inspection;

import java.io.Serializable;

/**
 * @ClassName InspectionPointPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 20:36
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class InspectionPointPo implements Serializable {

    private String inspectionId;
    private String inspectionName;
    private String machineId;
    private String communityId;
    private String remark;

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
