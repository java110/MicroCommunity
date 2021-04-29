package com.java110.fee.bmo.feeDiscountSpec;

import com.java110.po.feeDiscountSpec.FeeDiscountSpecPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeeDiscountSpecBMO {


    /**
     * 添加费用折扣
     * add by wuxw
     *
     * @param feeDiscountSpecPo
     * @return
     */
    ResponseEntity<String> save(FeeDiscountSpecPo feeDiscountSpecPo);


}
