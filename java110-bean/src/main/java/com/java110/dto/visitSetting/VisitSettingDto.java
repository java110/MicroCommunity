package com.java110.dto.visitSetting;

import com.java110.dto.PageDto;
import com.java110.dto.oaWorkflow.OaWorkflowDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 访客设置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class VisitSettingDto extends OaWorkflowDto implements Serializable {

    public static final String AUDIT_WAY_YES = "Y";
    public static final String AUDIT_WAY_NO = "N";

    private String carNumWay;
    private String faceWay;
    private String typeName;
    private String auditWay;
    private String remark;
    private String communityId;
    private String flowId;
    private String flowName;
    private String settingId;


    private Date createTime;

    private String statusCd = "0";


    private String paId;

    private String paNum;


    public String getCarNumWay() {
        return carNumWay;
    }

    public void setCarNumWay(String carNumWay) {
        this.carNumWay = carNumWay;
    }

    public String getFaceWay() {
        return faceWay;
    }

    public void setFaceWay(String faceWay) {
        this.faceWay = faceWay;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAuditWay() {
        return auditWay;
    }

    public void setAuditWay(String auditWay) {
        this.auditWay = auditWay;
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

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
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

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getPaNum() {
        return paNum;
    }

    public void setPaNum(String paNum) {
        this.paNum = paNum;
    }
}
