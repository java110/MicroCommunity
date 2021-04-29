package com.java110.fee.bmo.feeDiscountRuleSpec.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeDiscountRuleSpec.IDeleteFeeDiscountRuleSpecBMO;
import com.java110.intf.fee.IFeeDiscountRuleSpecInnerServiceSMO;
import com.java110.po.feeDiscountRuleSpec.FeeDiscountRuleSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeeDiscountRuleSpecBMOImpl")
public class DeleteFeeDiscountRuleSpecBMOImpl implements IDeleteFeeDiscountRuleSpecBMO {

    @Autowired
    private IFeeDiscountRuleSpecInnerServiceSMO feeDiscountRuleSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountRuleSpecPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(FeeDiscountRuleSpecPo feeDiscountRuleSpecPo) {

        int flag = feeDiscountRuleSpecInnerServiceSMOImpl.deleteFeeDiscountRuleSpec(feeDiscountRuleSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
