package com.java110.acct.bmo.shopVipAccountDetail;
import com.java110.dto.shopVipAccount.ShopUserAccountVipDto;
import com.java110.dto.shopVipAccount.ShopVipAccountDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetShopVipAccountDetailBMO {


    /**
     * 查询会员账户交易
     * add by wuxw
     * @param  shopVipAccountDetailDto
     * @return
     */
    ResponseEntity<String> get(ShopVipAccountDetailDto shopVipAccountDetailDto);



}
