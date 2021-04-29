package com.java110.goods.bmo.storeCart.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.storeCart.StoreCartDto;
import com.java110.goods.bmo.storeCart.ISaveStoreCartBMO;
import com.java110.intf.goods.IStoreCartInnerServiceSMO;
import com.java110.po.storeCart.StoreCartPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveStoreCartBMOImpl")
public class SaveStoreCartBMOImpl implements ISaveStoreCartBMO {

    @Autowired
    private IStoreCartInnerServiceSMO storeCartInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param storeCartPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(StoreCartPo storeCartPo) {

        storeCartPo.setCartId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_cartId));
        storeCartPo.setState(StoreCartDto.STATE_NO_BUY);
        int flag = storeCartInnerServiceSMOImpl.saveStoreCart(storeCartPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
