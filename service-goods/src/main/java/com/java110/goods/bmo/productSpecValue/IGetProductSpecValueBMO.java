package com.java110.goods.bmo.productSpecValue;
import com.java110.dto.productSpecValue.ProductSpecValueDto;
import org.springframework.http.ResponseEntity;
public interface IGetProductSpecValueBMO {


    /**
     * 查询产品规格值
     * add by wuxw
     * @param  productSpecValueDto
     * @return
     */
    ResponseEntity<String> get(ProductSpecValueDto productSpecValueDto);


}
