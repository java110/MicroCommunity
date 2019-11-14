package com.java110.dto.order;

import com.java110.dto.PageDto;

import java.io.Serializable;

public class OrderDto extends PageDto implements Serializable {
    private String oId;

    private String bId;

    private String businessTypeCd;


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
}
