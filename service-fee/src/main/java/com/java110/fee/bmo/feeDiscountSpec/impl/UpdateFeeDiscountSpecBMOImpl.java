package com.java110.fee.bmo.feeDiscountSpec.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeDiscountSpec.IUpdateFeeDiscountSpecBMO;
import com.java110.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.java110.po.feeDiscountSpec.FeeDiscountSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeDiscountSpecBMOImpl")
public class UpdateFeeDiscountSpecBMOImpl implements IUpdateFeeDiscountSpecBMO {

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountSpecPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(FeeDiscountSpecPo feeDiscountSpecPo) {

        int flag = feeDiscountSpecInnerServiceSMOImpl.updateFeeDiscountSpec(feeDiscountSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
