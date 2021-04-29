package com.java110.goods.bmo.storeOrderCartReturn.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.storeOrderCartReturn.IUpdateStoreOrderCartReturnBMO;
import com.java110.intf.goods.IStoreOrderCartReturnInnerServiceSMO;
import com.java110.po.storeOrderCartReturn.StoreOrderCartReturnPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateStoreOrderCartReturnBMOImpl")
public class UpdateStoreOrderCartReturnBMOImpl implements IUpdateStoreOrderCartReturnBMO {

    @Autowired
    private IStoreOrderCartReturnInnerServiceSMO storeOrderCartReturnInnerServiceSMOImpl;

    /**
     * @param storeOrderCartReturnPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(StoreOrderCartReturnPo storeOrderCartReturnPo) {

        int flag = storeOrderCartReturnInnerServiceSMOImpl.updateStoreOrderCartReturn(storeOrderCartReturnPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
