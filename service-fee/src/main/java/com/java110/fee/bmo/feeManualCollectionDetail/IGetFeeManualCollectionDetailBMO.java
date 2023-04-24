package com.java110.fee.bmo.feeManualCollectionDetail;

import com.java110.dto.fee.FeeManualCollectionDetailDto;
import org.springframework.http.ResponseEntity;

public interface IGetFeeManualCollectionDetailBMO {


    /**
     * 查询托收明细
     * add by wuxw
     *
     * @param feeManualCollectionDetailDto
     * @return
     */
    ResponseEntity<String> get(FeeManualCollectionDetailDto feeManualCollectionDetailDto);


}
