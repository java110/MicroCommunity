package com.java110.fee.bmo.feeReceipt;

import com.java110.dto.fee.FeeReceiptDto;
import com.java110.dto.fee.FeeReceiptDtoNew;
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

    /**
     * 查询收据按照户查
     * add by wuxw
     *
     * @param feeReceiptDto
     * @return
     */
    ResponseEntity<String> gets(FeeReceiptDtoNew feeReceiptDto);


}
