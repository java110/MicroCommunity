package com.java110.po.community;

import java.io.Serializable;

/**
 * @ClassName OrgCommunityPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 12:37
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class OrgCommunityPo implements Serializable {

    private String orgCommunityId;
    private String orgId;
    private String communityId;
    private String communityName;
    private String storeId;
    private String orgName;
    private String statusCd;


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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
