package com.java110.goods.bmo.storeOrderCartEvent;

import com.java110.po.storeOrderCartEvent.StoreOrderCartEventPo;
import org.springframework.http.ResponseEntity;
public interface ISaveStoreOrderCartEventBMO {


    /**
     * 添加购物车事件
     * add by wuxw
     * @param storeOrderCartEventPo
     * @return
     */
    ResponseEntity<String> save(StoreOrderCartEventPo storeOrderCartEventPo);


}
