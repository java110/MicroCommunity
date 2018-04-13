package com.java110.entity.user;

import java.util.List;

/**
 * 客户信息实体（过程）
 * Created by wuxw on 2016/12/27.
 */
public class BoCustOrder {

    //客户实体（过程）
    private BoCust boCust;

    //客户属性列表(过程)
    private List<BoCustAttr> boCustAttrList;


    public BoCust getBoCust() {
        return boCust;
    }

    public void setBoCust(BoCust boCust) {
        this.boCust = boCust;
    }

    public List<BoCustAttr> getBoCustAttrList() {
        return boCustAttrList;
    }

    public void setBoCustAttrList(List<BoCustAttr> boCustAttrList) {
        this.boCustAttrList = boCustAttrList;
    }
}
