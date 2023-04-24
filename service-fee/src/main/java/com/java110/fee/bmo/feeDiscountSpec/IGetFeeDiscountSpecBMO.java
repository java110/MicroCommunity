package com.java110.fee.bmo.feeDiscountSpec;

import com.java110.dto.fee.FeeDiscountSpecDto;
import org.springframework.http.ResponseEntity;

public interface IGetFeeDiscountSpecBMO {


    /**
     * 查询费用折扣
     * add by wuxw
     *
     * @param feeDiscountSpecDto
     * @return
     */
    ResponseEntity<String> get(FeeDiscountSpecDto feeDiscountSpecDto);


}
