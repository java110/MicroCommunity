package com.java110.fee.bmo.feeReceipt;

import com.java110.dto.feeReceipt.FeeReceiptDto;
import org.springframework.http.ResponseEntity;

public interface IGetFeeReceiptBMO {


    /**
     * 查询收据
     * add by wuxw
     *
     * @param feeReceiptDto
     * @return
     */
    ResponseEntity<String> get(FeeReceiptDto feeReceiptDto);


}
