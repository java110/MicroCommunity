package com.java110.goods.bmo.productSpec;
import com.java110.po.product.ProductSpecPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteProductSpecBMO {


    /**
     * 修改产品规格
     * add by wuxw
     * @param productSpecPo
     * @return
     */
    ResponseEntity<String> delete(ProductSpecPo productSpecPo);


}
