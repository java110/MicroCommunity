package com.java110.goods.bmo.storeOrder.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.storeOrder.IDeleteStoreOrderBMO;
import com.java110.intf.IStoreOrderInnerServiceSMO;
import com.java110.po.storeOrder.StoreOrderPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteStoreOrderBMOImpl")
public class DeleteStoreOrderBMOImpl implements IDeleteStoreOrderBMO {

    @Autowired
    private IStoreOrderInnerServiceSMO storeOrderInnerServiceSMOImpl;

    /**
     * @param storeOrderPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(StoreOrderPo storeOrderPo) {

        int flag = storeOrderInnerServiceSMOImpl.deleteStoreOrder(storeOrderPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
