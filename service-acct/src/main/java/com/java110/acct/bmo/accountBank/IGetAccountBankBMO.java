package com.java110.acct.bmo.accountBank;
import com.java110.dto.account.AccountBankDto;
import org.springframework.http.ResponseEntity;
public interface IGetAccountBankBMO {


    /**
     * 查询开户行
     * add by wuxw
     * @param  accountBankDto
     * @return
     */
    ResponseEntity<String> get(AccountBankDto accountBankDto);


}
