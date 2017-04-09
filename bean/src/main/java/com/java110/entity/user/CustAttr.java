package com.java110.entity.user;

/**
 * 客户属性
 * Created by wuxw on 2016/12/27.
 */
public class CustAttr {

    private String custId;

    private String itemSpecCd;

    private String value;

    private String status_cd;

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

    public String getStatus_cd() {
        return status_cd;
    }

    public void setStatus_cd(String status_cd) {
        this.status_cd = status_cd;
    }
}
