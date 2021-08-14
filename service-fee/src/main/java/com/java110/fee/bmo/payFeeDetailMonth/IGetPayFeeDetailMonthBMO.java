package com.java110.fee.bmo.payFeeDetailMonth;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailMonthDto;
import org.springframework.http.ResponseEntity;
public interface IGetPayFeeDetailMonthBMO {


    /**
     * 查询月缴费表
     * add by wuxw
     * @param  payFeeDetailMonthDto
     * @return
     */
    ResponseEntity<String> get(PayFeeDetailMonthDto payFeeDetailMonthDto);


}
