package com.java110.fee.bmo.payFeeConfigDiscount.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.payFeeConfigDiscount.PayFeeConfigDiscountDto;
import com.java110.fee.bmo.payFeeConfigDiscount.ISavePayFeeConfigDiscountBMO;
import com.java110.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.java110.po.payFeeConfigDiscount.PayFeeConfigDiscountPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePayFeeConfigDiscountBMOImpl")
public class SavePayFeeConfigDiscountBMOImpl implements ISavePayFeeConfigDiscountBMO {

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param payFeeConfigDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(PayFeeConfigDiscountPo payFeeConfigDiscountPo) {

        PayFeeConfigDiscountDto payFeeConfigDiscountDto = new PayFeeConfigDiscountDto();
        payFeeConfigDiscountDto.setConfigId(payFeeConfigDiscountPo.getConfigId());
        payFeeConfigDiscountDto.setDiscountId(payFeeConfigDiscountPo.getDiscountId());
        int i = payFeeConfigDiscountInnerServiceSMOImpl.queryPayFeeConfigDiscountsCount(payFeeConfigDiscountDto);
        if (i > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败，不能添加相同的折扣！");
        }

        payFeeConfigDiscountPo.setConfigDiscountId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configDiscountId));
        int flag = payFeeConfigDiscountInnerServiceSMOImpl.savePayFeeConfigDiscount(payFeeConfigDiscountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
