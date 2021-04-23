package com.java110.goods.bmo.storeOrderCartEvent;
import com.java110.dto.storeOrderCartEvent.StoreOrderCartEventDto;
import org.springframework.http.ResponseEntity;
public interface IGetStoreOrderCartEventBMO {


    /**
     * 查询购物车事件
     * add by wuxw
     * @param  storeOrderCartEventDto
     * @return
     */
    ResponseEntity<String> get(StoreOrderCartEventDto storeOrderCartEventDto);


}
