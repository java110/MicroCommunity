package com.java110.po.org;

import java.io.Serializable;

/**
 * @ClassName OrgPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 12:42
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class OrgPo implements Serializable {

    private String orgId;
    private String storeId;
    private String orgName;
    private String orgLevel;
    private String parentOrgId;
    private String description;

    private String belongCommunityId;
    private String allowOperation;
    private String statusCd = "0";

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getBelongCommunityId() {
        return belongCommunityId;
    }

    public void setBelongCommunityId(String belongCommunityId) {
        this.belongCommunityId = belongCommunityId;
    }

    public String getAllowOperation() {
        return allowOperation;
    }

    public void setAllowOperation(String allowOperation) {
        this.allowOperation = allowOperation;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
