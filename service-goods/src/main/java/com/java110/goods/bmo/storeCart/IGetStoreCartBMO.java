package com.java110.goods.bmo.storeCart;

import com.java110.dto.storeCart.StoreCartDto;
import org.springframework.http.ResponseEntity;

public interface IGetStoreCartBMO {


    /**
     * 查询购物车
     * add by wuxw
     *
     * @param storeCartDto
     * @return
     */
    ResponseEntity<String> get(StoreCartDto storeCartDto);


}
