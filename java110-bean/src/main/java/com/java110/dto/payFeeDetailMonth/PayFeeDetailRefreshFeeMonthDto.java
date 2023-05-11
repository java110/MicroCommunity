package com.java110.dto.payFeeDetailMonth;

import java.io.Serializable;

/**
 * 缴费离散月对象
 *
 */
public class PayFeeDetailRefreshFeeMonthDto implements Serializable {
    private String communityId;
    private String feeId;
    private String detailId;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
