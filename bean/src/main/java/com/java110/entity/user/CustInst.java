package com.java110.entity.user;

import java.util.List;

/**
 *  客户信息实体（实体）
 * Created by wuxw on 2016/12/27.
 */
public class CustInst {

    //客户实体
    private Cust cust;

    //客户属性列表
    private List<CustAttr> custAttrList;


    public Cust getCust() {
        return cust;
    }

    public void setCust(Cust cust) {
        this.cust = cust;
    }

    public List<CustAttr> getCustAttrList() {
        return custAttrList;
    }

    public void setCustAttrList(List<CustAttr> custAttrList) {
        this.custAttrList = custAttrList;
    }
}
