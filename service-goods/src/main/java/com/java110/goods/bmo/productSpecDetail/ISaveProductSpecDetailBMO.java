package com.java110.goods.bmo.productSpecDetail;

import com.java110.po.product.ProductSpecDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveProductSpecDetailBMO {


    /**
     * 添加产品规格明细
     * add by wuxw
     * @param productSpecDetailPo
     * @return
     */
    ResponseEntity<String> save(ProductSpecDetailPo productSpecDetailPo);


}
