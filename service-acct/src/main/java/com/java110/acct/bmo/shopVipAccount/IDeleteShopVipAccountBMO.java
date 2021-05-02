package com.java110.acct.bmo.shopVipAccount;
import com.java110.po.shopVipAccount.ShopVipAccountPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteShopVipAccountBMO {


    /**
     * 修改会员账户
     * add by wuxw
     * @param shopVipAccountPo
     * @return
     */
    ResponseEntity<String> delete(ShopVipAccountPo shopVipAccountPo);


}
