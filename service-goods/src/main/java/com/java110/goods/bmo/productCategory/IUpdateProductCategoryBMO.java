package com.java110.goods.bmo.productCategory;
import com.java110.po.product.ProductCategoryPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateProductCategoryBMO {


    /**
     * 修改产品目录
     * add by wuxw
     * @param productCategoryPo
     * @return
     */
    ResponseEntity<String> update(ProductCategoryPo productCategoryPo);


}
