package com.java110.dto;

import com.java110.dto.community.CommunityDto;

import java.io.Serializable;

/**
 * 小区成员dto
 */
public class CommunityMemberDto extends CommunityDto implements Serializable {


    public static final String AUDIT_STATUS_NORMAL = "1100"; // 审核通过

    public static final String MEMBER_TYPE_PROPERTY = "390001200002";

    private String communityMemberId;

    private String communityId;
    private String communityName;

    private String memberId;
    private String subMemberId;

    private String memberTypeCd;

    private String auditStatusCd;

    private String[] auditStatusCds;

    private String statusCd = "0";

    private boolean needCommunityInfo;

    private boolean noAuditEnterCommunity;

    private String startTime;
    private String endTime;

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

    public boolean isNoAuditEnterCommunity() {
        return noAuditEnterCommunity;
    }

    public void setNoAuditEnterCommunity(boolean noAuditEnterCommunity) {
        this.noAuditEnterCommunity = noAuditEnterCommunity;
    }

    public String[] getAuditStatusCds() {
        return auditStatusCds;
    }

    public void setAuditStatusCds(String[] auditStatusCds) {
        this.auditStatusCds = auditStatusCds;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getSubMemberId() {
        return subMemberId;
    }

    public void setSubMemberId(String subMemberId) {
        this.subMemberId = subMemberId;
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
}
