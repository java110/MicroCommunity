package com.java110.po.junkRequirement;

import java.io.Serializable;

/**
 * @ClassName JunkRequirementPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 22:45
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class JunkRequirementPo implements Serializable {

    private String junkRequirementId;
    private String typeCd;
    private String classification;
    private String context;
    private String referencePrice;
    private String publishUserId;
    private String publishUserName;
    private String publishUserLink;
    private String state;

    private String communityId;
    private String statusCd = "0";

    public String getJunkRequirementId() {
        return junkRequirementId;
    }

    public void setJunkRequirementId(String junkRequirementId) {
        this.junkRequirementId = junkRequirementId;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(String referencePrice) {
        this.referencePrice = referencePrice;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getPublishUserName() {
        return publishUserName;
    }

    public void setPublishUserName(String publishUserName) {
        this.publishUserName = publishUserName;
    }

    public String getPublishUserLink() {
        return publishUserLink;
    }

    public void setPublishUserLink(String publishUserLink) {
        this.publishUserLink = publishUserLink;
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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
