package com.java110.fee.bmo.payFeeConfigDiscount.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.payFeeConfigDiscount.IDeletePayFeeConfigDiscountBMO;
import com.java110.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.java110.po.payFeeConfigDiscount.PayFeeConfigDiscountPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deletePayFeeConfigDiscountBMOImpl")
public class DeletePayFeeConfigDiscountBMOImpl implements IDeletePayFeeConfigDiscountBMO {

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;

    /**
     * @param payFeeConfigDiscountPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(PayFeeConfigDiscountPo payFeeConfigDiscountPo) {

        int flag = payFeeConfigDiscountInnerServiceSMOImpl.deletePayFeeConfigDiscount(payFeeConfigDiscountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
