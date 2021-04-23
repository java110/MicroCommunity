package com.java110.dto.order;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * 订单项
 */
public class BusinessDto  extends PageDto implements Serializable {

    private String oId;

    private String bId;
    private String[] bIds;

    private String businessTypeCd;

    private String finishTime;

    private String statusCd;


    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String[] getbIds() {
        return bIds;
    }

    public void setbIds(String[] bIds) {
        this.bIds = bIds;
    }
}
