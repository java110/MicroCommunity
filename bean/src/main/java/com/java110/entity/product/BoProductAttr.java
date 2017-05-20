package com.java110.entity.product;

/**
 *
 * 产品属性 （过程表）
 *
 * Created by wuxw on 2017/5/20.
 */
public class BoProductAttr extends ProductAttr {

    private String boId;

    private String state;

    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
