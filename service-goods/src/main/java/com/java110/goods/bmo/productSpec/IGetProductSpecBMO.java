package com.java110.goods.bmo.productSpec;
import com.java110.dto.product.ProductSpecDto;
import org.springframework.http.ResponseEntity;
public interface IGetProductSpecBMO {


    /**
     * 查询产品规格
     * add by wuxw
     * @param  productSpecDto
     * @return
     */
    ResponseEntity<String> get(ProductSpecDto productSpecDto);


}
