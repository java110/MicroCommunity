package com.java110.acct.bmo.account;
import com.java110.dto.account.AccountDto;
import com.java110.dto.shopVipAccount.ShopVipAccountDto;
import org.springframework.http.ResponseEntity;

public interface IGetAccountBMO {


    /**
     * 查询账户信息
     * add by wuxw
     * @param  accountDto
     * @return
     */
    ResponseEntity<String> get(AccountDto accountDto);


}
