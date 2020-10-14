package com.java110.goods.bmo.groupBuyProduct;

import com.java110.dto.groupBuyProduct.GroupBuyProductDto;
import org.springframework.http.ResponseEntity;

public interface IGetGroupBuyProductBMO {


    /**
     * 查询拼团产品
     * add by wuxw
     *
     * @param groupBuyProductDto
     * @return
     */
    ResponseEntity<String> get(GroupBuyProductDto groupBuyProductDto);


}
