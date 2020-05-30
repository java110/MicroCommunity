package com.java110.po.fee;

import java.io.Serializable;

/**
 * @ClassName FeeAttrPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 21:42
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class FeeAttrPo implements Serializable {

    private String feeId;

    private String communityId;
    private String attrId;
    private String specCd;
    private String value;

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
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
}
