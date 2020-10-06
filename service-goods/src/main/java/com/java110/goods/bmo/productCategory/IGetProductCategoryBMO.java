package com.java110.goods.bmo.productCategory;
import com.java110.dto.productCategory.ProductCategoryDto;
import org.springframework.http.ResponseEntity;
public interface IGetProductCategoryBMO {


    /**
     * 查询产品目录
     * add by wuxw
     * @param  productCategoryDto
     * @return
     */
    ResponseEntity<String> get(ProductCategoryDto productCategoryDto);


}
