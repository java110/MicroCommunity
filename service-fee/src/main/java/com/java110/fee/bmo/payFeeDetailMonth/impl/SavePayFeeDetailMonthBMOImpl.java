package com.java110.fee.bmo.payFeeDetailMonth.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.payFeeDetailMonth.ISavePayFeeDetailMonthBMO;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.po.payFeeDetailMonth.PayFeeDetailMonthPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePayFeeDetailMonthBMOImpl")
public class SavePayFeeDetailMonthBMOImpl implements ISavePayFeeDetailMonthBMO {

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param payFeeDetailMonthPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(PayFeeDetailMonthPo payFeeDetailMonthPo) {

        payFeeDetailMonthPo.setMonthId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_monthId));
        int flag = payFeeDetailMonthInnerServiceSMOImpl.savePayFeeDetailMonth(payFeeDetailMonthPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
