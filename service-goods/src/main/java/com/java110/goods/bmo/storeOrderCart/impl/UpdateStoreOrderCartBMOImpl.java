package com.java110.goods.bmo.storeOrderCart.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.storeOrderCart.StoreOrderCartDto;
import com.java110.goods.bmo.storeOrderCart.IUpdateStoreOrderCartBMO;
import com.java110.intf.goods.IStoreOrderCartEventInnerServiceSMO;
import com.java110.intf.goods.IStoreOrderCartInnerServiceSMO;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import com.java110.po.storeOrderCartEvent.StoreOrderCartEventPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateStoreOrderCartBMOImpl")
public class UpdateStoreOrderCartBMOImpl implements IUpdateStoreOrderCartBMO {

    @Autowired
    private IStoreOrderCartInnerServiceSMO storeOrderCartInnerServiceSMOImpl;

    @Autowired
    private IStoreOrderCartEventInnerServiceSMO storeOrderCartEventInnerServiceSMOImpl;

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

    /**
     * 发货接口
     *
     * @param storeOrderCartPo 购物车信息
     * @param userId           操作员工
     * @return
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> sendOrderCart(StoreOrderCartPo storeOrderCartPo, String userId) {
        storeOrderCartPo.setState(StoreOrderCartDto.STATE_SENDING);
        int flag = storeOrderCartInnerServiceSMOImpl.updateStoreOrderCart(storeOrderCartPo);
        if (flag < 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        StoreOrderCartEventPo storeOrderCartEventPo = new StoreOrderCartEventPo();
        storeOrderCartEventPo.setCartId(storeOrderCartPo.getCartId());
        storeOrderCartEventPo.setOrderId(storeOrderCartPo.getOrderId());
        storeOrderCartEventPo.setEventId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_eventId));
        storeOrderCartEventPo.setEventObjType("S");
        storeOrderCartEventPo.setEventObjId(userId);
        storeOrderCartEventPo.setEventMsg("商家发货");
        flag = storeOrderCartEventInnerServiceSMOImpl.saveStoreOrderCartEvent(storeOrderCartEventPo);

        if (flag < 1) {
            throw new IllegalArgumentException("保存购物车事件失败");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
