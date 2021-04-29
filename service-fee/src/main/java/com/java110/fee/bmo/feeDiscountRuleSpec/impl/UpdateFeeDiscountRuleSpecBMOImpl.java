package com.java110.fee.bmo.feeDiscountRuleSpec.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeDiscountRuleSpec.IUpdateFeeDiscountRuleSpecBMO;
import com.java110.intf.fee.IFeeDiscountRuleSpecInnerServiceSMO;
import com.java110.po.feeDiscountRuleSpec.FeeDiscountRuleSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeDiscountRuleSpecBMOImpl")
public class UpdateFeeDiscountRuleSpecBMOImpl implements IUpdateFeeDiscountRuleSpecBMO {

    @Autowired
    private IFeeDiscountRuleSpecInnerServiceSMO feeDiscountRuleSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountRuleSpecPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(FeeDiscountRuleSpecPo feeDiscountRuleSpecPo) {

        int flag = feeDiscountRuleSpecInnerServiceSMOImpl.updateFeeDiscountRuleSpec(feeDiscountRuleSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
