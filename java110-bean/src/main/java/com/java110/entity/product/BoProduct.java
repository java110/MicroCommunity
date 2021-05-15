package com.java110.entity.product;


import java.util.List;

/**
 * Created by wuxw on 2017/5/20.
 */
public class BoProduct extends Product implements Comparable<BoProduct>{

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


    /**
     * 转为实例数据
     * @return
     */
    public Product convert(){

        Product product = new BoProduct();
        return product;
    }


    /**
     * 排序
     * @param o
     * @return
     */
    @Override
    public int compareTo(BoProduct otherBoProduct) {

        if("DEL".equals(this.getState()) && "ADD".equals(otherBoProduct.getState())) {
            return -1;
        }
        return 0;
    }
}
