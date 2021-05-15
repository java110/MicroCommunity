package com.java110.entity.order;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author wux
 * @create 2019-02-05 上午11:27
 * @desc 订单项 对应表c_business
 **/
public class Business extends BusinessPlus implements Comparable<Business>{


    /**
     * 订单ID
     */
    private String oId;

    /**
     * 订单项ID
     */
    private String bId;



    /**
     * 业务类型
     */
    private String businessTypeCd;

    List<BusinessAttrs> businessAttrs;


    private JSONObject data;


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

    public List<BusinessAttrs> getBusinessAttrs() {
        return businessAttrs;
    }

    public void setBusinessAttrs(List<BusinessAttrs> businessAttrs) {
        this.businessAttrs = businessAttrs;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public int compareTo(Business otherBusiness) {
        if(this.getSeq() < otherBusiness.getSeq()) {
            return -1;
        }
        return 0;
    }
}
