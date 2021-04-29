package com.java110.goods.bmo.storeOrderCartReturn;

import com.java110.dto.storeOrderCartReturn.StoreOrderCartReturnDto;
import org.springframework.http.ResponseEntity;

public interface IGetStoreOrderCartReturnBMO {


    /**
     * 查询购物车事件
     * add by wuxw
     *
     * @param storeOrderCartReturnDto
     * @return
     */
    ResponseEntity<String> get(StoreOrderCartReturnDto storeOrderCartReturnDto);


}
