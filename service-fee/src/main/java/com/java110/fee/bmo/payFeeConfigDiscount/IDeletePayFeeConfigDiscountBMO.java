package com.java110.fee.bmo.payFeeConfigDiscount;

import com.java110.po.payFeeConfigDiscount.PayFeeConfigDiscountPo;
import org.springframework.http.ResponseEntity;

public interface IDeletePayFeeConfigDiscountBMO {


    /**
     * 修改费用项折扣
     * add by wuxw
     *
     * @param payFeeConfigDiscountPo
     * @return
     */
    ResponseEntity<String> delete(PayFeeConfigDiscountPo payFeeConfigDiscountPo);


}
