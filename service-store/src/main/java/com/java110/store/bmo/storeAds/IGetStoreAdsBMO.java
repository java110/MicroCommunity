package com.java110.store.bmo.storeAds;
import com.java110.dto.storeAds.StoreAdsDto;
import org.springframework.http.ResponseEntity;
public interface IGetStoreAdsBMO {


    /**
     * 查询商户广告
     * add by wuxw
     * @param  storeAdsDto
     * @return
     */
    ResponseEntity<String> get(StoreAdsDto storeAdsDto);


}
