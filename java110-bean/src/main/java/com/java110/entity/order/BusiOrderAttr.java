package com.java110.entity.order;

import java.util.Date;

/**
 * Created by wuxw on 2017/4/9.
 */
public class BusiOrderAttr {


    //订单项ID
    private String boId;

    //属性编码，对应 Attr 表
    private String attrCd;
    //属性编码对应值
    private String value;
    //名称
    private String name;

    //创建时间
    private Date create_dt;

    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
    }

    public String getAttrCd() {
        return attrCd;
    }

    public void setAttrCd(String attrCd) {
        this.attrCd = attrCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }
}
