package com.java110.dto.shop;

import com.java110.dto.DefaultAttrEntity;

/**
 * Created by wuxw on 2017/5/20.
 */
public class ProductAttr extends DefaultAttrEntity {
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
