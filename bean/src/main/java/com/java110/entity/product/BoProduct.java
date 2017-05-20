package com.java110.entity.product;

import java.util.List;

/**
 * Created by wuxw on 2017/5/20.
 */
public class BoProduct extends Product {

    private String boId;

    private String state;

    private List<BoProductAttr> boProductAttrs;

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

    public List<BoProductAttr> getBoProductAttrs() {
        return boProductAttrs;
    }

    public void setBoProductAttrs(List<BoProductAttr> boProductAttrs) {
        this.boProductAttrs = boProductAttrs;
    }
}
