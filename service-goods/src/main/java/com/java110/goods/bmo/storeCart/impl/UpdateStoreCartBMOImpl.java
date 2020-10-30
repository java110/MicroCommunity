package com.java110.goods.bmo.storeCart.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.storeCart.IUpdateStoreCartBMO;
import com.java110.intf.goods.IStoreCartInnerServiceSMO;
import com.java110.po.storeCart.StoreCartPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateStoreCartBMOImpl")
public class UpdateStoreCartBMOImpl implements IUpdateStoreCartBMO {

    @Autowired
    private IStoreCartInnerServiceSMO storeCartInnerServiceSMOImpl;

    /**
     * @param storeCartPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(StoreCartPo storeCartPo) {

        int flag = storeCartInnerServiceSMOImpl.updateStoreCart(storeCartPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
