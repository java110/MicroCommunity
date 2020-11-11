package com.java110.goods.bmo.storeOrderAddress;
import com.java110.po.storeOrderAddress.StoreOrderAddressPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteStoreOrderAddressBMO {


    /**
     * 修改发货地址
     * add by wuxw
     * @param storeOrderAddressPo
     * @return
     */
    ResponseEntity<String> delete(StoreOrderAddressPo storeOrderAddressPo);


}
