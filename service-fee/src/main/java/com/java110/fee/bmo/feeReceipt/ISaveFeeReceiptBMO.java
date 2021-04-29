package com.java110.fee.bmo.feeReceipt;

import com.java110.po.feeReceipt.FeeReceiptPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeeReceiptBMO {


    /**
     * 添加收据
     * add by wuxw
     *
     * @param feeReceiptPo
     * @return
     */
    ResponseEntity<String> save(FeeReceiptPo feeReceiptPo);


}
