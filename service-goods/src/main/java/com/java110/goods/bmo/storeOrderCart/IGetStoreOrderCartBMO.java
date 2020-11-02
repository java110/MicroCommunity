package com.java110.goods.bmo.storeOrderCart;

import com.java110.dto.storeOrderCart.StoreOrderCartDto;
import org.springframework.http.ResponseEntity;

public interface IGetStoreOrderCartBMO {


    /**
     * 查询订单购物车
     * add by wuxw
     *
     * @param storeOrderCartDto
     * @return
     */
    ResponseEntity<String> get(StoreOrderCartDto storeOrderCartDto);


}
