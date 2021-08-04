package com.java110.store.bmo.storeAds.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IStoreAdsInnerServiceSMO;
import com.java110.po.storeAds.StoreAdsPo;
import com.java110.store.bmo.storeAds.IUpdateStoreAdsBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateStoreAdsBMOImpl")
public class UpdateStoreAdsBMOImpl implements IUpdateStoreAdsBMO {

    @Autowired
    private IStoreAdsInnerServiceSMO storeAdsInnerServiceSMOImpl;

    /**
     * @param storeAdsPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(StoreAdsPo storeAdsPo) {

        int flag = storeAdsInnerServiceSMOImpl.updateStoreAds(storeAdsPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
