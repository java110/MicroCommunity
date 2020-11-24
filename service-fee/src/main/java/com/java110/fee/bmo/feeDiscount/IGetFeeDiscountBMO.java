package com.java110.fee.bmo.feeDiscount;

import com.java110.dto.feeDiscount.FeeDiscountDto;
import org.springframework.http.ResponseEntity;

public interface IGetFeeDiscountBMO {


    /**
     * 查询费用折扣
     * add by wuxw
     *
     * @param feeDiscountDto
     * @return
     */
    ResponseEntity<String> get(FeeDiscountDto feeDiscountDto);


}
