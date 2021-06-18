package com.java110.acct.bmo.accountBank;
import com.java110.po.accountBank.AccountBankPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAccountBankBMO {


    /**
     * 修改开户行
     * add by wuxw
     * @param accountBankPo
     * @return
     */
    ResponseEntity<String> update(AccountBankPo accountBankPo);


}
