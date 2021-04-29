package com.java110.goods.bmo.groupBuyBatch;

import com.java110.po.groupBuyBatch.GroupBuyBatchPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteGroupBuyBatchBMO {


    /**
     * 修改拼团批次
     * add by wuxw
     *
     * @param groupBuyBatchPo
     * @return
     */
    ResponseEntity<String> delete(GroupBuyBatchPo groupBuyBatchPo);


}
