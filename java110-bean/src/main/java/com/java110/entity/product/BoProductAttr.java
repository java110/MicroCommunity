package com.java110.entity.product;

import com.java110.entity.product.BoProductAttr;

/**
 *
 * 产品属性 （过程表）
 *
 * Created by wuxw on 2017/5/20.
 */
public class BoProductAttr extends ProductAttr implements Comparable<BoProductAttr>{

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

    /**
     * 将过程数据转为实例数据
     * @return
     */
    public ProductAttr convert(){
        ProductAttr productAttr = new BoProductAttr();
        return productAttr;
    }

    @Override
    public int compareTo(BoProductAttr otherBoProduct) {
        if("DEL".equals(this.getState()) && "ADD".equals(otherBoProduct.getState())) {
            return -1;
        }
        return 0;
    }
}
