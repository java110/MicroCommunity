package com.java110.entity.product;

import com.java110.entity.DefaultAttrEntity;
import com.java110.entity.DefaultEntity;

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
