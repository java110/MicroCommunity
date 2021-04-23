package com.java110.goods.bmo.storeOrderCartReturnEvent;

import com.java110.dto.storeOrderCartReturnEvent.StoreOrderCartReturnEventDto;
import org.springframework.http.ResponseEntity;

public interface IGetStoreOrderCartReturnEventBMO {


    /**
     * 查询退货事件
     * add by wuxw
     *
     * @param storeOrderCartReturnEventDto
     * @return
     */
    ResponseEntity<String> get(StoreOrderCartReturnEventDto storeOrderCartReturnEventDto);


}
