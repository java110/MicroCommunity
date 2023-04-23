package com.java110.fee.bmo.feeDiscount;

import com.java110.dto.fee.FeeDiscountDto;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;
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


    /**
     * 查询 缴费优惠
     * @param payFeeDetailDiscountDto
     * @return
     */
    ResponseEntity<String> getFeeDetailDiscount(PayFeeDetailDiscountDto payFeeDetailDiscountDto);
}
