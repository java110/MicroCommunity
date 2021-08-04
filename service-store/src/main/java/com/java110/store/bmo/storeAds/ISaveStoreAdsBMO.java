package com.java110.store.bmo.storeAds;

import com.java110.po.storeAds.StoreAdsPo;
import org.springframework.http.ResponseEntity;
public interface ISaveStoreAdsBMO {


    /**
     * 添加商户广告
     * add by wuxw
     * @param storeAdsPo
     * @return
     */
    ResponseEntity<String> save(StoreAdsPo storeAdsPo);


}
