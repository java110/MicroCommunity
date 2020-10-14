package com.java110.goods.bmo.groupBuy;

import com.java110.dto.groupBuy.GroupBuyDto;
import org.springframework.http.ResponseEntity;

public interface IGetGroupBuyBMO {


    /**
     * 查询拼团购买
     * add by wuxw
     *
     * @param groupBuyDto
     * @return
     */
    ResponseEntity<String> get(GroupBuyDto groupBuyDto);


}
