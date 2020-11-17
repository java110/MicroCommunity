package com.java110.goods.bmo.storeOrderCartReturnEvent;

import com.java110.po.storeOrderCartReturnEvent.StoreOrderCartReturnEventPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteStoreOrderCartReturnEventBMO {


    /**
     * 修改退货事件
     * add by wuxw
     *
     * @param storeOrderCartReturnEventPo
     * @return
     */
    ResponseEntity<String> delete(StoreOrderCartReturnEventPo storeOrderCartReturnEventPo);


}
