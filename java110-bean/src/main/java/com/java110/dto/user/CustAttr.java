package com.java110.dto.user;

import com.java110.dto.DefaultAttrEntity;

/**
 * 客户属性
 * Created by wuxw on 2016/12/27.
 */
public class CustAttr extends DefaultAttrEntity{

    private String custId;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

}
