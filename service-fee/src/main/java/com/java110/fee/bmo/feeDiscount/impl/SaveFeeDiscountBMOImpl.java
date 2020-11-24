package com.java110.fee.bmo.feeDiscount.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.feeDiscount.ISaveFeeDiscountBMO;
import com.java110.intf.fee.IFeeDiscountInnerServiceSMO;
import com.java110.po.feeDiscount.FeeDiscountPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeDiscountBMOImpl")
public class SaveFeeDiscountBMOImpl implements ISaveFeeDiscountBMO {

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(FeeDiscountPo feeDiscountPo) {

        feeDiscountPo.setDiscountId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_discountId));
        int flag = feeDiscountInnerServiceSMOImpl.saveFeeDiscount(feeDiscountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
