package com.java110.goods.bmo.groupBuy;

import com.java110.po.groupBuy.GroupBuyPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteGroupBuyBMO {


    /**
     * 修改拼团购买
     * add by wuxw
     *
     * @param groupBuyPo
     * @return
     */
    ResponseEntity<String> delete(GroupBuyPo groupBuyPo);


}
