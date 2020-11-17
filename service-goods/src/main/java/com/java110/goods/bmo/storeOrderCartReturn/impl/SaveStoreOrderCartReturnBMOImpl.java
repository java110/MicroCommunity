package com.java110.goods.bmo.storeOrderCartReturn.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.storeOrderCartReturn.ISaveStoreOrderCartReturnBMO;
import com.java110.intf.goods.IStoreOrderCartReturnInnerServiceSMO;
import com.java110.po.storeOrderCartReturn.StoreOrderCartReturnPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveStoreOrderCartReturnBMOImpl")
public class SaveStoreOrderCartReturnBMOImpl implements ISaveStoreOrderCartReturnBMO {

    @Autowired
    private IStoreOrderCartReturnInnerServiceSMO storeOrderCartReturnInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param storeOrderCartReturnPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(StoreOrderCartReturnPo storeOrderCartReturnPo) {

        storeOrderCartReturnPo.setReturnId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_returnId));
        int flag = storeOrderCartReturnInnerServiceSMOImpl.saveStoreOrderCartReturn(storeOrderCartReturnPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
