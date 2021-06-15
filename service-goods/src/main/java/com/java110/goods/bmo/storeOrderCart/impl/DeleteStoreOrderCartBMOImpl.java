package com.java110.goods.bmo.storeOrderCart.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.storeOrderCart.IDeleteStoreOrderCartBMO;
import com.java110.intf.goods.IStoreOrderCartInnerServiceSMO;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteStoreOrderCartBMOImpl")
public class DeleteStoreOrderCartBMOImpl implements IDeleteStoreOrderCartBMO {

    @Autowired
    private IStoreOrderCartInnerServiceSMO storeOrderCartInnerServiceSMOImpl;

    /**
     * @param storeOrderCartPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(StoreOrderCartPo storeOrderCartPo) {

        int flag = storeOrderCartInnerServiceSMOImpl.deleteStoreOrderCart(storeOrderCartPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
