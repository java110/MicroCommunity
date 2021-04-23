package com.java110.fee.bmo.feeManualCollection;

import com.java110.po.feeManualCollection.FeeManualCollectionPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeeManualCollectionBMO {


    /**
     * 添加人工托收
     * add by wuxw
     *
     * @param feeManualCollectionPo
     * @return
     */
    ResponseEntity<String> save(FeeManualCollectionPo feeManualCollectionPo);


}
