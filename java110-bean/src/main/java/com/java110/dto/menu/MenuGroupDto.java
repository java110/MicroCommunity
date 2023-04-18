package com.java110.dto.menu;

import com.java110.dto.PageDto;

import java.io.Serializable;

public class MenuGroupDto extends PageDto implements Serializable {

    public static final String GROUP_TYPE_PC = "P_WEB";

    public static final String STORE_TYPE_PROPERTY = "800900000003"; // 物业

    private String gId;
    private String[] gIds;
    private String name;
    private String icon;
    private String label;
    private String seq;
    private String description;
    private String statusCd;
    private String groupType;
    private String storeType;
    private String userId;
    private String domain;
    private String caId;
    private String communityId;


    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCaId() {
        return caId;
    }

    public void setCaId(String caId) {
        this.caId = caId;
    }

    public String[] getgIds() {
        return gIds;
    }

    public void setgIds(String[] gIds) {
        this.gIds = gIds;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
