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
    private String communityId;
    private String pointObjType;
    private String pointObjId;
    private String pointObjName;
    private String pointStartTime;
    private String pointEndTime;
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


    public String getPointObjType() {
        return pointObjType;
    }

    public void setPointObjType(String pointObjType) {
        this.pointObjType = pointObjType;
    }

    public String getPointObjId() {
        return pointObjId;
    }

    public void setPointObjId(String pointObjId) {
        this.pointObjId = pointObjId;
    }

    public String getPointObjName() {
        return pointObjName;
    }

    public void setPointObjName(String pointObjName) {
        this.pointObjName = pointObjName;
    }

    public String getPointStartTime() {
        return pointStartTime;
    }

    public void setPointStartTime(String pointStartTime) {
        this.pointStartTime = pointStartTime;
    }

    public String getPointEndTime() {
        return pointEndTime;
    }

    public void setPointEndTime(String pointEndTime) {
        this.pointEndTime = pointEndTime;
    }
}
