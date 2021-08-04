package com.java110.store.bmo.storeAds;
import com.java110.po.storeAds.StoreAdsPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteStoreAdsBMO {


    /**
     * 修改商户广告
     * add by wuxw
     * @param storeAdsPo
     * @return
     */
    ResponseEntity<String> delete(StoreAdsPo storeAdsPo);


}
