package com.java110.fee.bmo.feeManualCollectionDetail;

import com.java110.dto.feeManualCollection.FeeManualCollectionDetailDto;
import org.springframework.http.ResponseEntity;

public interface IGetExportCollectionBMO {


    /**
     * 查询托收明细
     * add by wuxw
     *
     * @param feeManualCollectionDetailDto
     * @return
     */
    ResponseEntity<String> get(FeeManualCollectionDetailDto feeManualCollectionDetailDto);


}
