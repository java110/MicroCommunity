package com.java110.fee.bmo.feeReceiptDetail;

import com.java110.po.fee.FeeReceiptDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveFeeReceiptDetailBMO {


    /**
     * 添加收据明细
     * add by wuxw
     * @param feeReceiptDetailPo
     * @return
     */
    ResponseEntity<String> save(FeeReceiptDetailPo feeReceiptDetailPo);


}
