package com.java110.goods.bmo.storeOrderCartEvent.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.storeOrderCartEvent.ISaveStoreOrderCartEventBMO;
import com.java110.intf.goods.IStoreOrderCartEventInnerServiceSMO;
import com.java110.po.storeOrderCartEvent.StoreOrderCartEventPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveStoreOrderCartEventBMOImpl")
public class SaveStoreOrderCartEventBMOImpl implements ISaveStoreOrderCartEventBMO {

    @Autowired
    private IStoreOrderCartEventInnerServiceSMO storeOrderCartEventInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param storeOrderCartEventPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(StoreOrderCartEventPo storeOrderCartEventPo) {

        storeOrderCartEventPo.setEventId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_eventId));
        int flag = storeOrderCartEventInnerServiceSMOImpl.saveStoreOrderCartEvent(storeOrderCartEventPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
