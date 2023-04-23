package com.java110.fee.bmo.feeManualCollection;

import com.java110.dto.fee.FeeManualCollectionDto;
import org.springframework.http.ResponseEntity;

public interface IGetFeeManualCollectionBMO {


    /**
     * 查询人工托收
     * add by wuxw
     *
     * @param feeManualCollectionDto
     * @return
     */
    ResponseEntity<String> get(FeeManualCollectionDto feeManualCollectionDto);


}
