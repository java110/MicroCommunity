package com.java110.fee.bmo.feeManualCollectionDetail;

import com.java110.po.feeManualCollectionDetail.FeeManualCollectionDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeManualCollectionDetailBMO {


    /**
     * 修改托收明细
     * add by wuxw
     *
     * @param feeManualCollectionDetailPo
     * @return
     */
    ResponseEntity<String> delete(FeeManualCollectionDetailPo feeManualCollectionDetailPo);


}
