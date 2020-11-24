package com.java110.fee.bmo.feeDiscount.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeDiscount.IDeleteFeeDiscountBMO;
import com.java110.intf.fee.IFeeDiscountInnerServiceSMO;
import com.java110.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.java110.po.feeDiscount.FeeDiscountPo;
import com.java110.po.feeDiscountSpec.FeeDiscountSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeeDiscountBMOImpl")
public class DeleteFeeDiscountBMOImpl implements IDeleteFeeDiscountBMO {

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(FeeDiscountPo feeDiscountPo) {

        int flag = feeDiscountInnerServiceSMOImpl.deleteFeeDiscount(feeDiscountPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");

        }

        FeeDiscountSpecPo feeDiscountSpecPo = null;
        //删除所有
        feeDiscountSpecPo = new FeeDiscountSpecPo();
        feeDiscountSpecPo.setCommunityId(feeDiscountPo.getCommunityId());
        feeDiscountSpecPo.setDiscountId(feeDiscountPo.getDiscountId());
        feeDiscountSpecInnerServiceSMOImpl.deleteFeeDiscountSpec(feeDiscountSpecPo);

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
