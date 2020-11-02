package com.java110.goods.bmo.storeOrder;

import com.java110.po.storeOrder.StoreOrderPo;
import org.springframework.http.ResponseEntity;
public interface ISaveStoreOrderBMO {


    /**
     * 添加购物车
     * add by wuxw
     * @param storeOrderPo
     * @return
     */
    ResponseEntity<String> save(StoreOrderPo storeOrderPo);


}
