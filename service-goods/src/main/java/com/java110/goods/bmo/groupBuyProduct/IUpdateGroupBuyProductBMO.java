package com.java110.goods.bmo.groupBuyProduct;

import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.po.groupBuyProductSpec.GroupBuyProductSpecPo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUpdateGroupBuyProductBMO {


    /**
     * 修改拼团产品
     * add by wuxw
     *
     * @param groupBuyProductPo
     * @return
     */
    ResponseEntity<String> update(GroupBuyProductPo groupBuyProductPo, List<GroupBuyProductSpecPo> groupBuyProductSpecPos);


}
