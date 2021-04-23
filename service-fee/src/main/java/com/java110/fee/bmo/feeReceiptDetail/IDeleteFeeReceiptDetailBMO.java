package com.java110.fee.bmo.feeReceiptDetail;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeReceiptDetailBMO {


    /**
     * 修改收据明细
     * add by wuxw
     * @param feeReceiptDetailPo
     * @return
     */
    ResponseEntity<String> delete(FeeReceiptDetailPo feeReceiptDetailPo);


}
