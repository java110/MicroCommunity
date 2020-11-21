package com.java110.goods.bmo.storeOrderCartReturn.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.storeOrderCartReturn.IDeleteStoreOrderCartReturnBMO;
import com.java110.intf.goods.IStoreOrderCartReturnInnerServiceSMO;
import com.java110.po.storeOrderCartReturn.StoreOrderCartReturnPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteStoreOrderCartReturnBMOImpl")
public class DeleteStoreOrderCartReturnBMOImpl implements IDeleteStoreOrderCartReturnBMO {

    @Autowired
    private IStoreOrderCartReturnInnerServiceSMO storeOrderCartReturnInnerServiceSMOImpl;

    /**
     * @param storeOrderCartReturnPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(StoreOrderCartReturnPo storeOrderCartReturnPo) {

        int flag = storeOrderCartReturnInnerServiceSMOImpl.deleteStoreOrderCartReturn(storeOrderCartReturnPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
