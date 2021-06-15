package com.java110.goods.bmo.storeOrderAddress;
import com.java110.dto.storeOrderAddress.StoreOrderAddressDto;
import org.springframework.http.ResponseEntity;
public interface IGetStoreOrderAddressBMO {


    /**
     * 查询发货地址
     * add by wuxw
     * @param  storeOrderAddressDto
     * @return
     */
    ResponseEntity<String> get(StoreOrderAddressDto storeOrderAddressDto);


}
