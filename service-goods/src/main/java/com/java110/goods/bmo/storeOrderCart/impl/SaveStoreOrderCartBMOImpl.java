package com.java110.goods.bmo.storeOrderCart.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.storeOrderCart.ISaveStoreOrderCartBMO;
import com.java110.intf.IStoreOrderCartInnerServiceSMO;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveStoreOrderCartBMOImpl")
public class SaveStoreOrderCartBMOImpl implements ISaveStoreOrderCartBMO {

    @Autowired
    private IStoreOrderCartInnerServiceSMO storeOrderCartInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param storeOrderCartPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(StoreOrderCartPo storeOrderCartPo) {

        storeOrderCartPo.setCartId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_cartId));
        int flag = storeOrderCartInnerServiceSMOImpl.saveStoreOrderCart(storeOrderCartPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
