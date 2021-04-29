package com.java110.goods.bmo.storeOrderAddress;

import com.java110.po.storeOrderAddress.StoreOrderAddressPo;
import org.springframework.http.ResponseEntity;
public interface ISaveStoreOrderAddressBMO {


    /**
     * 添加发货地址
     * add by wuxw
     * @param storeOrderAddressPo
     * @return
     */
    ResponseEntity<String> save(StoreOrderAddressPo storeOrderAddressPo);


}
