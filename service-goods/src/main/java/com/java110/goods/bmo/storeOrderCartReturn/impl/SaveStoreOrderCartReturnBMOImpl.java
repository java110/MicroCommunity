package com.java110.goods.bmo.storeOrderCartReturn.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.storeOrderCart.StoreOrderCartDto;
import com.java110.goods.bmo.storeOrderCartReturn.ISaveStoreOrderCartReturnBMO;
import com.java110.intf.goods.IStoreOrderCartInnerServiceSMO;
import com.java110.intf.goods.IStoreOrderCartReturnEventInnerServiceSMO;
import com.java110.intf.goods.IStoreOrderCartReturnInnerServiceSMO;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import com.java110.po.storeOrderCartReturn.StoreOrderCartReturnPo;
import com.java110.po.storeOrderCartReturnEvent.StoreOrderCartReturnEventPo;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveStoreOrderCartReturnBMOImpl")
public class SaveStoreOrderCartReturnBMOImpl implements ISaveStoreOrderCartReturnBMO {

    @Autowired
    private IStoreOrderCartReturnInnerServiceSMO storeOrderCartReturnInnerServiceSMOImpl;

    @Autowired
    private IStoreOrderCartReturnEventInnerServiceSMO storeOrderCartReturnEventInnerServiceSMOImpl;

    @Autowired
    private IStoreOrderCartInnerServiceSMO storeOrderCartInnerServiceSMOImpl;

    /**
     * 申请退款
     *
     * @param storeOrderCartReturnPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(StoreOrderCartReturnPo storeOrderCartReturnPo) {

        //查询
        StoreOrderCartDto storeOrderCartDto = new StoreOrderCartDto();
        storeOrderCartDto.setCartId(storeOrderCartDto.getCartId());
        storeOrderCartDto.setStoreId(storeOrderCartReturnPo.getStoreId());
        storeOrderCartDto.setState(StoreOrderCartDto.STATE_SENDING);

        List<StoreOrderCartDto> storeOrderCartDtos = storeOrderCartInnerServiceSMOImpl.queryStoreOrderCarts(storeOrderCartDto);

        Assert.listOnlyOne(storeOrderCartDtos, "当前不符合退货条件");

        storeOrderCartReturnPo.setReturnId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_returnId));
        int flag = storeOrderCartReturnInnerServiceSMOImpl.saveStoreOrderCartReturn(storeOrderCartReturnPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        StoreOrderCartReturnEventPo storeOrderCartReturnEventPo = new StoreOrderCartReturnEventPo();
        storeOrderCartReturnEventPo.setEventId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_eventId));
        storeOrderCartReturnEventPo.setEventMsg("用户申请退货");
        storeOrderCartReturnEventPo.setEventObjId(storeOrderCartReturnPo.getPersonId());
        storeOrderCartReturnEventPo.setEventObjType("U");//用户申请
        storeOrderCartReturnEventPo.setReturnId(storeOrderCartReturnPo.getReturnId());
        storeOrderCartReturnEventPo.setStoreId(storeOrderCartReturnPo.getStoreId());

        flag = storeOrderCartReturnEventInnerServiceSMOImpl.saveStoreOrderCartReturnEvent(storeOrderCartReturnEventPo);
        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        StoreOrderCartPo storeOrderCartPo = new StoreOrderCartPo();
        storeOrderCartPo.setCartId(storeOrderCartDtos.get(0).getCartId());
        storeOrderCartPo.setState(StoreOrderCartDto.STATE_REQ_RETURN);
        storeOrderCartPo.setStoreId(storeOrderCartDtos.get(0).getStoreId());
        flag = storeOrderCartInnerServiceSMOImpl.updateStoreOrderCart(storeOrderCartPo);
        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
