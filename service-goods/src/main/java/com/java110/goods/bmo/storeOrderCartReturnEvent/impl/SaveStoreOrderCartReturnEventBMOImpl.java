package com.java110.goods.bmo.storeOrderCartReturnEvent.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.storeOrderCartReturnEvent.ISaveStoreOrderCartReturnEventBMO;
import com.java110.intf.goods.IStoreOrderCartReturnEventInnerServiceSMO;
import com.java110.po.storeOrderCartReturnEvent.StoreOrderCartReturnEventPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveStoreOrderCartReturnEventBMOImpl")
public class SaveStoreOrderCartReturnEventBMOImpl implements ISaveStoreOrderCartReturnEventBMO {

    @Autowired
    private IStoreOrderCartReturnEventInnerServiceSMO storeOrderCartReturnEventInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param storeOrderCartReturnEventPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(StoreOrderCartReturnEventPo storeOrderCartReturnEventPo) {

        storeOrderCartReturnEventPo.setEventId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_eventId));
        int flag = storeOrderCartReturnEventInnerServiceSMOImpl.saveStoreOrderCartReturnEvent(storeOrderCartReturnEventPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
