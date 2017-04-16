package com.java110.entity.order;

import java.util.Date;

/**
 * 购物车属性表
 * Created by wuxw on 2017/4/9.
 */
public class OrderListAttr {

    //购物车ID
    private String olId;

    //属性编码，对应 Attr 表
    private String attrCd;
    //属性编码对应值
    private String value;
    //名称
    private String name;

    //创建时间
    private Date create_dt;

    public String getOlId() {
        return olId;
    }

    public void setOlId(String olId) {
        this.olId = olId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
