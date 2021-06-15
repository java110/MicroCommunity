package com.java110.goods.bmo.storeOrder;
import com.java110.dto.storeOrder.StoreOrderDto;
import org.springframework.http.ResponseEntity;
public interface IGetStoreOrderBMO {


    /**
     * 查询购物车
     * add by wuxw
     * @param  storeOrderDto
     * @return
     */
    ResponseEntity<String> get(StoreOrderDto storeOrderDto);


}
