package com.java110.fee.bmo.feeReceiptDetail;
import com.java110.dto.fee.FeeReceiptDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetFeeReceiptDetailBMO {


    /**
     * 查询收据明细
     * add by wuxw
     * @param  feeReceiptDetailDto
     * @return
     */
    ResponseEntity<String> get(FeeReceiptDetailDto feeReceiptDetailDto);


}
