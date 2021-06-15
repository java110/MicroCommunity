package com.java110.vo.api.corder;

import java.io.Serializable;

public class CbusinessVo implements Serializable {
    private String bId;
    private String oId;
    private String createTime;
    private String businessTypeCd;
    private String finishTime;
    private String remark;
    private String statusCd;
    private String businessTypeCdName;

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBusinessTypeCd() {
        return businessTypeCd;
    }

    public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getBusinessTypeCdName() {
        return businessTypeCdName;
    }

    public void setBusinessTypeCdName(String businessTypeCdName) {
        this.businessTypeCdName = businessTypeCdName;
    }
}
