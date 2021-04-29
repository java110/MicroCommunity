package com.java110.fee.bmo.feeDiscountSpec;

import com.java110.po.feeDiscountSpec.FeeDiscountSpecPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeDiscountSpecBMO {


    /**
     * 修改费用折扣
     * add by wuxw
     *
     * @param feeDiscountSpecPo
     * @return
     */
    ResponseEntity<String> delete(FeeDiscountSpecPo feeDiscountSpecPo);


}
