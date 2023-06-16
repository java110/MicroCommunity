package com.java110.acct.bmo.shopVipAccount;
import com.java110.po.shop.ShopVipAccountPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateShopVipAccountBMO {


    /**
     * 修改会员账户
     * add by wuxw
     * @param shopVipAccountPo
     * @return
     */
    ResponseEntity<String> update(ShopVipAccountPo shopVipAccountPo);


}
