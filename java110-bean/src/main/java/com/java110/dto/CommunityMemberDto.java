package com.java110.dto;

import com.java110.dto.community.CommunityDto;

import java.io.Serializable;

/**
 * 小区成员dto
 */
public class CommunityMemberDto extends CommunityDto implements Serializable {

    private String communityMemberId;

    private String communityId;

    private String memberId;

    private String memberTypeCd;

    private String auditStatusCd;

    private String statusCd;

    private boolean needCommunityInfo;

    public String getCommunityMemberId() {
        return communityMemberId;
    }

    public void setCommunityMemberId(String communityMemberId) {
        this.communityMemberId = communityMemberId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberTypeCd() {
        return memberTypeCd;
    }

    public void setMemberTypeCd(String memberTypeCd) {
        this.memberTypeCd = memberTypeCd;
    }

    public String getAuditStatusCd() {
        return auditStatusCd;
    }

    public void setAuditStatusCd(String auditStatusCd) {
        this.auditStatusCd = auditStatusCd;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public boolean isNeedCommunityInfo() {
        return needCommunityInfo;
    }

    public void setNeedCommunityInfo(boolean needCommunityInfo) {
        this.needCommunityInfo = needCommunityInfo;
    }
}
