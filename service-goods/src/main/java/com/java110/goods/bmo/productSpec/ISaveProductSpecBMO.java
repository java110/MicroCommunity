package com.java110.goods.bmo.productSpec;

import com.java110.po.product.ProductSpecPo;
import org.springframework.http.ResponseEntity;
public interface ISaveProductSpecBMO {


    /**
     * 添加产品规格
     * add by wuxw
     * @param productSpecPo
     * @return
     */
    ResponseEntity<String> save(ProductSpecPo productSpecPo);


}
