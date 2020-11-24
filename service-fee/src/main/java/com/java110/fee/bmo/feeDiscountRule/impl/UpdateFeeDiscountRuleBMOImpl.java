package com.java110.fee.bmo.feeDiscountRule.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeDiscountRule.IUpdateFeeDiscountRuleBMO;
import com.java110.intf.fee.IFeeDiscountRuleInnerServiceSMO;
import com.java110.po.feeDiscountRule.FeeDiscountRulePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeDiscountRuleBMOImpl")
public class UpdateFeeDiscountRuleBMOImpl implements IUpdateFeeDiscountRuleBMO {

    @Autowired
    private IFeeDiscountRuleInnerServiceSMO feeDiscountRuleInnerServiceSMOImpl;

    /**
     * @param feeDiscountRulePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(FeeDiscountRulePo feeDiscountRulePo) {

        int flag = feeDiscountRuleInnerServiceSMOImpl.updateFeeDiscountRule(feeDiscountRulePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
