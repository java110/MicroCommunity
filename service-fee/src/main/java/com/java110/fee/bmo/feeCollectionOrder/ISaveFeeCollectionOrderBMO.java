package com.java110.fee.bmo.feeCollectionOrder;

import com.java110.po.feeCollectionOrder.FeeCollectionOrderPo;
import org.springframework.http.ResponseEntity;
public interface ISaveFeeCollectionOrderBMO {


    /**
     * 添加催缴单
     * add by wuxw
     * @param feeCollectionOrderPo
     * @return
     */
    ResponseEntity<String> save(FeeCollectionOrderPo feeCollectionOrderPo);


}
