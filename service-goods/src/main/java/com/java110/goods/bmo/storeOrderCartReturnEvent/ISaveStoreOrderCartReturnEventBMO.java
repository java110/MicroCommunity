package com.java110.goods.bmo.storeOrderCartReturnEvent;

import com.java110.po.storeOrderCartReturnEvent.StoreOrderCartReturnEventPo;
import org.springframework.http.ResponseEntity;
public interface ISaveStoreOrderCartReturnEventBMO {


    /**
     * 添加退货事件
     * add by wuxw
     * @param storeOrderCartReturnEventPo
     * @return
     */
    ResponseEntity<String> save(StoreOrderCartReturnEventPo storeOrderCartReturnEventPo);


}
