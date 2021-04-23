package com.java110.po.importFee;

import java.io.Serializable;

public class ImportFeePo implements Serializable {

    private String feeTypeCd;
    private String statusCd = "0";
    private String importFeeId;
    private String communityId;
    private String remark;

    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getImportFeeId() {
        return importFeeId;
    }

    public void setImportFeeId(String importFeeId) {
        this.importFeeId = importFeeId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
