package com.java110.goods.bmo.groupBuy;

import com.java110.po.groupBuy.GroupBuyPo;
import org.springframework.http.ResponseEntity;

public interface ISaveGroupBuyBMO {


    /**
     * 添加拼团购买
     * add by wuxw
     *
     * @param groupBuyPo
     * @return
     */
    ResponseEntity<String> save(GroupBuyPo groupBuyPo);


}
