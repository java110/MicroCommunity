package com.java110.fee.bmo.feeCollectionOrder.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.feeCollectionOrder.ISaveFeeCollectionOrderBMO;
import com.java110.intf.fee.IFeeCollectionOrderInnerServiceSMO;
import com.java110.po.feeCollectionOrder.FeeCollectionOrderPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeCollectionOrderBMOImpl")
public class SaveFeeCollectionOrderBMOImpl implements ISaveFeeCollectionOrderBMO {

    @Autowired
    private IFeeCollectionOrderInnerServiceSMO feeCollectionOrderInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeCollectionOrderPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(FeeCollectionOrderPo feeCollectionOrderPo) {

        feeCollectionOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        int flag = feeCollectionOrderInnerServiceSMOImpl.saveFeeCollectionOrder(feeCollectionOrderPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
