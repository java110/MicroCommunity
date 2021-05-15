package com.java110.entity.user;

import com.java110.entity.DefaultBoAttrEntity;

import java.util.Date;

/**
 * 客户属性表（过程表）
 * Created by wuxw on 2016/12/27.
 */
public class BoCustAttr extends DefaultBoAttrEntity implements Comparable<BoCustAttr>{

    private String custId;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
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
    public int compareTo(BoCustAttr otherBoCust) {
        if("DEL".equals(this.getState()) && "ADD".equals(otherBoCust.getState())) {
            return -1;
        }
        return 0;
    }
}
