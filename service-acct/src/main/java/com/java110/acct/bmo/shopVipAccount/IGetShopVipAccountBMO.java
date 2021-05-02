package com.java110.acct.bmo.shopVipAccount;
import com.java110.dto.shopVipAccount.ShopVipAccountDto;
import org.springframework.http.ResponseEntity;
public interface IGetShopVipAccountBMO {


    /**
     * 查询会员账户
     * add by wuxw
     * @param  shopVipAccountDto
     * @return
     */
    ResponseEntity<String> get(ShopVipAccountDto shopVipAccountDto);


}
