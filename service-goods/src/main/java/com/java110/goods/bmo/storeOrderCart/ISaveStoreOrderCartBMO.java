package com.java110.goods.bmo.storeOrderCart;

import com.java110.po.storeOrderCart.StoreOrderCartPo;
import org.springframework.http.ResponseEntity;

public interface ISaveStoreOrderCartBMO {


    /**
     * 添加订单购物车
     * add by wuxw
     *
     * @param storeOrderCartPo
     * @return
     */
    ResponseEntity<String> save(StoreOrderCartPo storeOrderCartPo);


}
