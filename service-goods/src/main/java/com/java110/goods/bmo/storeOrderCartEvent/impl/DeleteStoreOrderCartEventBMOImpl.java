package com.java110.goods.bmo.storeOrderCartEvent.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.storeOrderCartEvent.IDeleteStoreOrderCartEventBMO;
import com.java110.intf.goods.IStoreOrderCartEventInnerServiceSMO;
import com.java110.po.storeOrderCartEvent.StoreOrderCartEventPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteStoreOrderCartEventBMOImpl")
public class DeleteStoreOrderCartEventBMOImpl implements IDeleteStoreOrderCartEventBMO {

    @Autowired
    private IStoreOrderCartEventInnerServiceSMO storeOrderCartEventInnerServiceSMOImpl;

    /**
     * @param storeOrderCartEventPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(StoreOrderCartEventPo storeOrderCartEventPo) {

        int flag = storeOrderCartEventInnerServiceSMOImpl.deleteStoreOrderCartEvent(storeOrderCartEventPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
