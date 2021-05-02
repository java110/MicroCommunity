package com.java110.acct.bmo.shopVipAccount;

import com.java110.po.shopVipAccount.ShopVipAccountPo;
import org.springframework.http.ResponseEntity;
public interface ISaveShopVipAccountBMO {


    /**
     * 添加会员账户
     * add by wuxw
     * @param shopVipAccountPo
     * @return
     */
    ResponseEntity<String> save(ShopVipAccountPo shopVipAccountPo);


}
