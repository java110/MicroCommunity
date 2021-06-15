package com.java110.goods.bmo.groupBuyProduct;

import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.po.groupBuyProductSpec.GroupBuyProductSpecPo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISaveGroupBuyProductBMO {


    /**
     * 添加拼团产品
     * add by wuxw
     *
     * @param groupBuyProductPo
     * @return
     */
    ResponseEntity<String> save(GroupBuyProductPo groupBuyProductPo, List<GroupBuyProductSpecPo> groupBuyProductSpecPos);


}
