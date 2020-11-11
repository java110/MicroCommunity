package com.java110.goods.bmo.storeOrderCart.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.storeOrderCart.IUpdateStoreOrderCartBMO;
import com.java110.intf.goods.IStoreOrderCartInnerServiceSMO;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateStoreOrderCartBMOImpl")
public class UpdateStoreOrderCartBMOImpl implements IUpdateStoreOrderCartBMO {

    @Autowired
    private IStoreOrderCartInnerServiceSMO storeOrderCartInnerServiceSMOImpl;

    /**
     * @param storeOrderCartPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(StoreOrderCartPo storeOrderCartPo) {

        int flag = storeOrderCartInnerServiceSMOImpl.updateStoreOrderCart(storeOrderCartPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
