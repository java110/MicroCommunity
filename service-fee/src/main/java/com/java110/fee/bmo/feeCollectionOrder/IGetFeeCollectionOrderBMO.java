package com.java110.fee.bmo.feeCollectionOrder;
import com.java110.dto.fee.FeeCollectionOrderDto;
import org.springframework.http.ResponseEntity;
public interface IGetFeeCollectionOrderBMO {


    /**
     * 查询催缴单
     * add by wuxw
     * @param  feeCollectionOrderDto
     * @return
     */
    ResponseEntity<String> get(FeeCollectionOrderDto feeCollectionOrderDto);


}
