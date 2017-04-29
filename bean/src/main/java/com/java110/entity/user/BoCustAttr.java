package com.java110.entity.user;

import java.util.Date;

/**
 * 客户属性表（过程表）
 * Created by wuxw on 2016/12/27.
 */
public class BoCustAttr implements Comparable{

    private String boId;

    private String custId;

    private String attrCd;

    private String value;

    private String state;

    private Date create_dt;

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

    public String getAttrCd() {
        return attrCd;
    }

    public void setAttrCd(String attrCd) {
        this.attrCd = attrCd;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }

    /**
     * 将过程数据转为实例数据
     * @return
     */
    public CustAttr convert(){
        CustAttr custAttr = new CustAttr();

        custAttr.setCustId(this.getCustId());
        custAttr.setAttrCd(this.getAttrCd());
        custAttr.setValue(this.getValue());
        return custAttr;
    }

    @Override
    public int compareTo(Object o) {
        BoCustAttr otherBoCust = (BoCustAttr)o;
        if("DEL".equals(this.getState()) && "ADD".equals(otherBoCust.getState())) {
            return -1;
        }
        return 0;
    }
}
