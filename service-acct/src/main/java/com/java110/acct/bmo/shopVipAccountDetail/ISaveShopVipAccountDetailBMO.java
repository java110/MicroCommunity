package com.java110.acct.bmo.shopVipAccountDetail;

import com.java110.po.shopVipAccountDetail.ShopVipAccountDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveShopVipAccountDetailBMO {


    /**
     * 添加会员账户交易
     * add by wuxw
     * @param shopVipAccountDetailPo
     * @return
     */
    ResponseEntity<String> save(ShopVipAccountDetailPo shopVipAccountDetailPo);


}
