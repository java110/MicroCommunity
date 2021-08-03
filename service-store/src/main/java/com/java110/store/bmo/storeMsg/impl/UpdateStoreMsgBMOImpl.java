package com.java110.store.bmo.storeMsg.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IStoreMsgInnerServiceSMO;
import com.java110.po.storeMsg.StoreMsgPo;
import com.java110.store.bmo.storeMsg.IUpdateStoreMsgBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateStoreMsgBMOImpl")
public class UpdateStoreMsgBMOImpl implements IUpdateStoreMsgBMO {

    @Autowired
    private IStoreMsgInnerServiceSMO storeMsgInnerServiceSMOImpl;

    /**
     * @param storeMsgPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(StoreMsgPo storeMsgPo) {

        int flag = storeMsgInnerServiceSMOImpl.updateStoreMsg(storeMsgPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
