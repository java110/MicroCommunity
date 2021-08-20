package com.java110.fee.bmo.payFeeDetailMonth;

import com.java110.po.payFeeDetailMonth.PayFeeDetailMonthPo;
import org.springframework.http.ResponseEntity;
public interface ISavePayFeeDetailMonthBMO {


    /**
     * 添加月缴费表
     * add by wuxw
     * @param payFeeDetailMonthPo
     * @return
     */
    ResponseEntity<String> save(PayFeeDetailMonthPo payFeeDetailMonthPo);


}
