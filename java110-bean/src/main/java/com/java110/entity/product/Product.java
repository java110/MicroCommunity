package com.java110.entity.product;

import com.java110.entity.DefaultEntity;

import java.util.List;

/**
 * 产品实例的公用属性
 *
 *
 * Created by wuxw on 2017/5/20.
 */
public class Product extends DefaultEntity {

    private String productId;

    private String  productImg;

    private String name;

    private String catalogCd;

    private String productDesc;

    private List<ProductAttr> productAttrs;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalogCd() {
        return catalogCd;
    }

    public void setCatalogCd(String catalogCd) {
        this.catalogCd = catalogCd;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
}
