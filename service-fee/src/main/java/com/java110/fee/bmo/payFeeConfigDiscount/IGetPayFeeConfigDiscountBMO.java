package com.java110.fee.bmo.payFeeConfigDiscount;

import com.java110.dto.payFeeConfigDiscount.PayFeeConfigDiscountDto;
import org.springframework.http.ResponseEntity;

public interface IGetPayFeeConfigDiscountBMO {


    /**
     * 查询费用项折扣
     * add by wuxw
     *
     * @param payFeeConfigDiscountDto
     * @return
     */
    ResponseEntity<String> get(PayFeeConfigDiscountDto payFeeConfigDiscountDto);


}
