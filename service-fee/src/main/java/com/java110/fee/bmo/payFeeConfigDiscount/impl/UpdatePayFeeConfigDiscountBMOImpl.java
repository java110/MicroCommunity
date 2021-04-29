package com.java110.fee.bmo.payFeeConfigDiscount.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.payFeeConfigDiscount.IUpdatePayFeeConfigDiscountBMO;
import com.java110.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.java110.po.payFeeConfigDiscount.PayFeeConfigDiscountPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePayFeeConfigDiscountBMOImpl")
public class UpdatePayFeeConfigDiscountBMOImpl implements IUpdatePayFeeConfigDiscountBMO {

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;

    /**
     * @param payFeeConfigDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(PayFeeConfigDiscountPo payFeeConfigDiscountPo) {

        int flag = payFeeConfigDiscountInnerServiceSMOImpl.updatePayFeeConfigDiscount(payFeeConfigDiscountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
