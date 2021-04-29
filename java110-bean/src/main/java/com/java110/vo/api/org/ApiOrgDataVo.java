package com.java110.vo.api.org;

import java.io.Serializable;
import java.util.Date;

public class ApiOrgDataVo implements Serializable {

    private String orgId;
    private String orgName;
    private String orgLevel;
    private String parentOrgId;
    private String parentOrgName;
    private String belongCommunityId;
    private String belongCommunityName;
    private String description;
    private String allowOperation;
    private String staffId;

    private String orgLevelName;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
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
}
