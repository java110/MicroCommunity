package com.java110.entity.order;

/**
 * @author wux
 * @create 2019-02-05 上午11:20
 * @desc 订单属性 对应 表c_orders_attrs
 **/
public class Attrs {

    private String attrId;

    private String specCd;

    private String value;


    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getSpecCd() {
        return specCd;
    }

    public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
