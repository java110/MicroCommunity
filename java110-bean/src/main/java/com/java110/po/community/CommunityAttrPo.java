package com.java110.po.community;

import java.io.Serializable;

/**
 * @ClassName CommunityAttrPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 21:36
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class CommunityAttrPo implements Serializable {

    private String attrId;
    private String communityId;
    private String specCd;
    private String value;
    private String statusCd;

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSpecCd() {
        return specCd;
    }

    public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
