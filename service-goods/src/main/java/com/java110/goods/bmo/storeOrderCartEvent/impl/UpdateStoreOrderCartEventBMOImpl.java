package com.java110.goods.bmo.storeOrderCartEvent.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.storeOrderCartEvent.IUpdateStoreOrderCartEventBMO;
import com.java110.intf.goods.IStoreOrderCartEventInnerServiceSMO;
import com.java110.po.storeOrderCartEvent.StoreOrderCartEventPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateStoreOrderCartEventBMOImpl")
public class UpdateStoreOrderCartEventBMOImpl implements IUpdateStoreOrderCartEventBMO {

    @Autowired
    private IStoreOrderCartEventInnerServiceSMO storeOrderCartEventInnerServiceSMOImpl;

    /**
     * @param storeOrderCartEventPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(StoreOrderCartEventPo storeOrderCartEventPo) {

        int flag = storeOrderCartEventInnerServiceSMOImpl.updateStoreOrderCartEvent(storeOrderCartEventPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
