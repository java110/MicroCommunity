package com.java110.acct.bmo.accountBond;

import com.java110.po.accountBond.AccountBondPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAccountBondBMO {


    /**
     * 添加保证金
     * add by wuxw
     * @param accountBondPo
     * @return
     */
    ResponseEntity<String> save(AccountBondPo accountBondPo);


}
