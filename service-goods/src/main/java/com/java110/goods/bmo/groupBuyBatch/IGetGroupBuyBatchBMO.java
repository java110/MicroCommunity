package com.java110.goods.bmo.groupBuyBatch;

import com.java110.dto.groupBuyBatch.GroupBuyBatchDto;
import org.springframework.http.ResponseEntity;

public interface IGetGroupBuyBatchBMO {


    /**
     * 查询拼团批次
     * add by wuxw
     *
     * @param groupBuyBatchDto
     * @return
     */
    ResponseEntity<String> get(GroupBuyBatchDto groupBuyBatchDto);


}
