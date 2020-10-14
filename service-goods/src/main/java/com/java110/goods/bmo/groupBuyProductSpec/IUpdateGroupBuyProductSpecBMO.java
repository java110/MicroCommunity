package com.java110.goods.bmo.groupBuyProductSpec;

import com.java110.po.groupBuyProductSpec.GroupBuyProductSpecPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateGroupBuyProductSpecBMO {


    /**
     * 修改拼团产品规格
     * add by wuxw
     *
     * @param groupBuyProductSpecPo
     * @return
     */
    ResponseEntity<String> update(GroupBuyProductSpecPo groupBuyProductSpecPo);


}
