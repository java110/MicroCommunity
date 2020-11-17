package com.java110.goods.bmo.storeOrderCartReturn;

import com.java110.po.storeOrderCartReturn.StoreOrderCartReturnPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateStoreOrderCartReturnBMO {


    /**
     * 修改购物车事件
     * add by wuxw
     *
     * @param storeOrderCartReturnPo
     * @return
     */
    ResponseEntity<String> update(StoreOrderCartReturnPo storeOrderCartReturnPo);


}
