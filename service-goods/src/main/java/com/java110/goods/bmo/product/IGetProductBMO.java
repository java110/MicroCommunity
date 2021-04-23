package com.java110.goods.bmo.product;
import com.java110.dto.product.ProductDto;
import org.springframework.http.ResponseEntity;
public interface IGetProductBMO {


    /**
     * 查询产品
     * add by wuxw
     * @param  productDto
     * @return
     */
    ResponseEntity<String> get(ProductDto productDto);


}
