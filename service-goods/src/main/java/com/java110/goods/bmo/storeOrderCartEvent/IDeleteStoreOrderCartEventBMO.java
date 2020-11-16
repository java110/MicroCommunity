package com.java110.goods.bmo.storeOrderCartEvent;

import com.java110.po.storeOrderCartEvent.StoreOrderCartEventPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteStoreOrderCartEventBMO {


    /**
     * 修改购物车事件
     * add by wuxw
     *
     * @param storeOrderCartEventPo
     * @return
     */
    ResponseEntity<String> delete(StoreOrderCartEventPo storeOrderCartEventPo);


}
