package com.java110.dto.assetInventory;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 盘点管理数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AssetInventoryDto extends PageDto implements Serializable {

    private String code;
private String shId;
private String remark;
private String aiId;
private String invTime;
private String name;
private String staffName;
private String shName;
private String state;
private String stateName;
private String communityId;
private String staffId;
    private String opinion;
    private String auditId;
    private String auditName;
    private String auditTel;
    private String auditTime;
    private Date createTime;

    private String statusCd = "0";


    public String getCode() {
        return code;
    }
public void setCode(String code) {
        this.code = code;
    }
public String getShId() {
        return shId;
    }
public void setShId(String shId) {
        this.shId = shId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getAiId() {
        return aiId;
    }
public void setAiId(String aiId) {
        this.aiId = aiId;
    }
public String getInvTime() {
        return invTime;
    }
public void setInvTime(String invTime) {
        this.invTime = invTime;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getStaffName() {
        return staffName;
    }
public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
public String getShName() {
        return shName;
    }
public void setShName(String shName) {
        this.shName = shName;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
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

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getAuditTel() {
        return auditTel;
    }

    public void setAuditTel(String auditTel) {
        this.auditTel = auditTel;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }
}
