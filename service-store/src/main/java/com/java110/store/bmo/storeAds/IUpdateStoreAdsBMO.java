package com.java110.store.bmo.storeAds;
import com.java110.po.storeAds.StoreAdsPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateStoreAdsBMO {


    /**
     * 修改商户广告
     * add by wuxw
     * @param storeAdsPo
     * @return
     */
    ResponseEntity<String> update(StoreAdsPo storeAdsPo);


}
