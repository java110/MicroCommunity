package com.java110.goods.bmo.productLabel;

import com.java110.dto.productLabel.ProductLabelDto;
import org.springframework.http.ResponseEntity;

public interface IGetProductLabelBMO {


    /**
     * 查询产品标签
     * add by wuxw
     *
     * @param productLabelDto
     * @return
     */
    ResponseEntity<String> get(ProductLabelDto productLabelDto);


}
