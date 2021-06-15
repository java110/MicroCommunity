package com.java110.po.inspection;

import java.io.Serializable;

/**
 * @ClassName InspectionPlanPo
 * @Description TODO 巡检计划对象
 * @Author wuxw
 * @Date 2020/5/28 14:23
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class InspectionPlanPo implements Serializable {

    private String inspectionPlanId;
    private String inspectionPlanName;
    private String inspectionRouteId;
    private String communityId;
    private String startTime;
    private String endTime;
    private String inspectionPlanPeriod;
    private String signType;
    private String state;
    private String remark;
    private String createUserId;
    private String createUserName;

    public String getInspectionPlanId() {
        return inspectionPlanId;
    }

    public void setInspectionPlanId(String inspectionPlanId) {
        this.inspectionPlanId = inspectionPlanId;
    }

    public String getInspectionPlanName() {
        return inspectionPlanName;
    }

    public void setInspectionPlanName(String inspectionPlanName) {
        this.inspectionPlanName = inspectionPlanName;
    }

    public String getInspectionRouteId() {
        return inspectionRouteId;
    }

    public void setInspectionRouteId(String inspectionRouteId) {
        this.inspectionRouteId = inspectionRouteId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInspectionPlanPeriod() {
        return inspectionPlanPeriod;
    }

    public void setInspectionPlanPeriod(String inspectionPlanPeriod) {
        this.inspectionPlanPeriod = inspectionPlanPeriod;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}
