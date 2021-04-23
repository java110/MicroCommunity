package com.java110.goods.bmo.product;
import com.java110.po.product.ProductPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteProductBMO {


    /**
     * 修改产品
     * add by wuxw
     * @param productPo
     * @return
     */
    ResponseEntity<String> delete(ProductPo productPo);


}
