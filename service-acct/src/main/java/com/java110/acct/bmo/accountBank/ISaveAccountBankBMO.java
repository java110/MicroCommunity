package com.java110.acct.bmo.accountBank;

import com.java110.po.accountBank.AccountBankPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAccountBankBMO {


    /**
     * 添加开户行
     * add by wuxw
     * @param accountBankPo
     * @return
     */
    ResponseEntity<String> save(AccountBankPo accountBankPo);


}
