package com.java110.fee.bmo.feeDiscount;

import com.java110.po.feeDiscount.FeeDiscountPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeDiscountBMO {


    /**
     * 修改费用折扣
     * add by wuxw
     *
     * @param feeDiscountPo
     * @return
     */
    ResponseEntity<String> delete(FeeDiscountPo feeDiscountPo);


}
