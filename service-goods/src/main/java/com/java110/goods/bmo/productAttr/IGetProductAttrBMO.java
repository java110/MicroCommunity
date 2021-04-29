package com.java110.goods.bmo.productAttr;
import com.java110.dto.productAttr.ProductAttrDto;
import org.springframework.http.ResponseEntity;
public interface IGetProductAttrBMO {


    /**
     * 查询产品属性
     * add by wuxw
     * @param  productAttrDto
     * @return
     */
    ResponseEntity<String> get(ProductAttrDto productAttrDto);


}
