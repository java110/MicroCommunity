package com.java110.goods.bmo.productSpec;
import com.java110.po.product.ProductSpecPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateProductSpecBMO {


    /**
     * 修改产品规格
     * add by wuxw
     * @param productSpecPo
     * @return
     */
    ResponseEntity<String> update(ProductSpecPo productSpecPo);


}
