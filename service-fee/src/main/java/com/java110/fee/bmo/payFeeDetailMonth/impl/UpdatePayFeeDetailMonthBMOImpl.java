package com.java110.fee.bmo.payFeeDetailMonth.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.payFeeDetailMonth.IUpdatePayFeeDetailMonthBMO;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.po.payFeeDetailMonth.PayFeeDetailMonthPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePayFeeDetailMonthBMOImpl")
public class UpdatePayFeeDetailMonthBMOImpl implements IUpdatePayFeeDetailMonthBMO {

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;

    /**
     * @param payFeeDetailMonthPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(PayFeeDetailMonthPo payFeeDetailMonthPo) {

        int flag = payFeeDetailMonthInnerServiceSMOImpl.updatePayFeeDetailMonth(payFeeDetailMonthPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
