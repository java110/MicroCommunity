package com.java110.po.community;

import java.io.Serializable;

/**
 * @ClassName CommunityMemberPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/27 14:49
 * @Version 1.0
 * add by wuxw 2020/5/27
 **/
public class CommunityMemberPo implements Serializable {

    private String communityMemberId;
    private String communityId;
    private String memberId;
    private String memberTypeCd;
    private String auditStatusCd;

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
}
