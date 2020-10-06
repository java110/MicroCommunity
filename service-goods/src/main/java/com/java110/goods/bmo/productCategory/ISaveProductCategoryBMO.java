package com.java110.goods.bmo.productCategory;

import com.java110.po.productCategory.ProductCategoryPo;
import org.springframework.http.ResponseEntity;
public interface ISaveProductCategoryBMO {


    /**
     * 添加产品目录
     * add by wuxw
     * @param productCategoryPo
     * @return
     */
    ResponseEntity<String> save(ProductCategoryPo productCategoryPo);


}
