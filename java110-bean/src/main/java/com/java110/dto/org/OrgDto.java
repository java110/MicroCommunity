package com.java110.dto.org;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 组织数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OrgDto extends PageDto implements Serializable {

    public static final String ORG_LEVEL_STORE = "1";
    public static final String ORG_LEVEL_COMPANY = "2";
    public static final String ORG_LEVEL_DEPARTMENT = "3";

    private String orgName;
    private String parentOrgId;
    private String description;
    private String orgLevel;
    private String storeId;
    private String orgId;
    private String[] orgIds;
    private String belongCommunityId;
    private String belongCommunityName;
    private String allowOperation;
    private String staffId;
    private String staffName;

    private String parentOrgName;

    private String orgLevelName;


    private Date createTime;

    private String statusCd = "0";


    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public String getOrgLevelName() {
        return orgLevelName;
    }

    public void setOrgLevelName(String orgLevelName) {
        this.orgLevelName = orgLevelName;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }


    public String getBelongCommunityId() {
        return belongCommunityId;
    }

    public void setBelongCommunityId(String belongCommunityId) {
        this.belongCommunityId = belongCommunityId;
    }

    public String getBelongCommunityName() {
        return belongCommunityName;
    }

    public void setBelongCommunityName(String belongCommunityName) {
        this.belongCommunityName = belongCommunityName;
    }

    public String getAllowOperation() {
        return allowOperation;
    }

    public void setAllowOperation(String allowOperation) {
        this.allowOperation = allowOperation;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String[] getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String[] orgIds) {
        this.orgIds = orgIds;
    }
}
