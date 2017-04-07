package com.java110.bean.user;

/**
 * 客户属性表（过程表）
 * Created by wuxw on 2016/12/27.
 */
public class BoCustAttr {

    private String boId;

    private String custId;

    private String itemSpecCd;

    private String value;

    private String state;

    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getItemSpecCd() {
        return itemSpecCd;
    }

    public void setItemSpecCd(String itemSpecCd) {
        this.itemSpecCd = itemSpecCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
