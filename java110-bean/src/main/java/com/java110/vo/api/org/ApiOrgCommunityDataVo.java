package com.java110.vo.api.org;

import java.io.Serializable;

public class ApiOrgCommunityDataVo implements Serializable {

    private String orgCommunityId;
    private String orgId;
    private String orgName;
    private String communityId;
    private String communityName;


    public String getOrgCommunityId() {
        return orgCommunityId;
    }

    public void setOrgCommunityId(String orgCommunityId) {
        this.orgCommunityId = orgCommunityId;
    }

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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
