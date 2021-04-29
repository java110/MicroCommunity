package com.java110.goods.bmo.storeCart;

import com.java110.po.storeCart.StoreCartPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateStoreCartBMO {


    /**
     * 修改购物车
     * add by wuxw
     *
     * @param storeCartPo
     * @return
     */
    ResponseEntity<String> update(StoreCartPo storeCartPo);


}
