package com.java110.goods.bmo.storeOrderCart;

import com.java110.po.storeOrderCart.StoreOrderCartPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateStoreOrderCartBMO {


    /**
     * 修改订单购物车
     * add by wuxw
     *
     * @param storeOrderCartPo
     * @return
     */
    ResponseEntity<String> update(StoreOrderCartPo storeOrderCartPo);


}
