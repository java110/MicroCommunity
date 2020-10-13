package com.java110.goods.bmo.productDetail;
import com.java110.dto.productDetail.ProductDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetProductDetailBMO {


    /**
     * 查询产品属性
     * add by wuxw
     * @param  productDetailDto
     * @return
     */
    ResponseEntity<String> get(ProductDetailDto productDetailDto);


}
