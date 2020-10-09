package com.java110.goods.bmo.productSpecValue;

import com.java110.po.productSpecValue.ProductSpecValuePo;
import org.springframework.http.ResponseEntity;
public interface ISaveProductSpecValueBMO {


    /**
     * 添加产品规格值
     * add by wuxw
     * @param productSpecValuePo
     * @return
     */
    ResponseEntity<String> save(ProductSpecValuePo productSpecValuePo);


}
