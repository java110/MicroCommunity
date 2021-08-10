package com.java110.store.bmo.storeAds.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IStoreAdsInnerServiceSMO;
import com.java110.po.storeAds.StoreAdsPo;
import com.java110.store.bmo.storeAds.IDeleteStoreAdsBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteStoreAdsBMOImpl")
public class DeleteStoreAdsBMOImpl implements IDeleteStoreAdsBMO {

    @Autowired
    private IStoreAdsInnerServiceSMO storeAdsInnerServiceSMOImpl;

    /**
     * @param storeAdsPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(StoreAdsPo storeAdsPo) {

        int flag = storeAdsInnerServiceSMOImpl.deleteStoreAds(storeAdsPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
