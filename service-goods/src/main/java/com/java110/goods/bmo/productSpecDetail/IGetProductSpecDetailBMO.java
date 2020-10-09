package com.java110.goods.bmo.productSpecDetail;
import com.java110.dto.product.ProductSpecDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetProductSpecDetailBMO {


    /**
     * 查询产品规格明细
     * add by wuxw
     * @param  productSpecDetailDto
     * @return
     */
    ResponseEntity<String> get(ProductSpecDetailDto productSpecDetailDto);


}
