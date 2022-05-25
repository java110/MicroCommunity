package com.java110.po.feeAccountDetail;

import java.io.Serializable;

public class FeeAccountDetailPo implements Serializable {

    private String fadId;

    private String detailId;

    private String communityId;

    private String createTime;

    private String statusCd = "0";

    private String state;

    private String amount;

    public String getFadId() {
        return fadId;
    }

    public void setFadId(String fadId) {
        this.fadId = fadId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
