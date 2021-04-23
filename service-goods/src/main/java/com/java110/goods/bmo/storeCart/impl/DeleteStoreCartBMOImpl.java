package com.java110.goods.bmo.storeCart.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.storeCart.IDeleteStoreCartBMO;
import com.java110.intf.goods.IStoreCartInnerServiceSMO;
import com.java110.po.storeCart.StoreCartPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteStoreCartBMOImpl")
public class DeleteStoreCartBMOImpl implements IDeleteStoreCartBMO {

    @Autowired
    private IStoreCartInnerServiceSMO storeCartInnerServiceSMOImpl;

    /**
     * @param storeCartPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(StoreCartPo storeCartPo) {

        int flag = storeCartInnerServiceSMOImpl.deleteStoreCart(storeCartPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
