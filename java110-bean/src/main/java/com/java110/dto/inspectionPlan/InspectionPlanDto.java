package com.java110.dto.inspectionPlan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java110.dto.PageDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 巡检计划数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InspectionPlanDto extends PageDto implements Serializable {

    public static final String STATE_STOP = "2020026";
    public static final String STATE_RUN = "2020025";

    public static final String INSPECTION_PLAN_PERIOD_DAY = "2020022"; // 连续每天
    public static final String INSPECTION_PLAN_PERIOD_WEEK = "2020023"; // 连续每周
    public static final String INSPECTION_PLAN_PERIOD_NEXT_DAY = "2020024"; // 连续隔天

    private String inspectionPlanName;
    private String inspectionRouteId;
    private String inspectionRouteName;
    private String inspectionPlanPeriod;
    private String remark;
    private String endTime;
    private String staffName;
    private String signType;
    private String startTime;
    private String createUserId;
    private String createUserName;
    private String inspectionPlanId;
    private String state;
    private String communityId;
    private String staffId;
    private Date createTime;
    private String statusCd = "0";
    private String stateName;
    private String inspectionPlanPeriodName;
    private String signTypeName;
    private String keyWord;


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

    public String getInspectionPlanPeriod() {
        return inspectionPlanPeriod;
    }

    public void setInspectionPlanPeriod(String inspectionPlanPeriod) {
        this.inspectionPlanPeriod = inspectionPlanPeriod;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getInspectionPlanId() {
        return inspectionPlanId;
    }

    public void setInspectionPlanId(String inspectionPlanId) {
        this.inspectionPlanId = inspectionPlanId;
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

    public String getInspectionRouteName() {
        return inspectionRouteName;
    }

    public void setInspectionRouteName(String inspectionRouteName) {
        this.inspectionRouteName = inspectionRouteName;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getInspectionPlanPeriodName() {
        return inspectionPlanPeriodName;
    }

    public void setInspectionPlanPeriodName(String inspectionPlanPeriodName) {
        this.inspectionPlanPeriodName = inspectionPlanPeriodName;
    }

    public String getSignTypeName() {
        return signTypeName;
    }

    public void setSignTypeName(String signTypeName) {
        this.signTypeName = signTypeName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
