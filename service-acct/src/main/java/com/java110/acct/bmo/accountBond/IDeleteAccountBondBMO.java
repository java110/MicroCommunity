package com.java110.acct.bmo.accountBond;
import com.java110.po.accountBond.AccountBondPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteAccountBondBMO {


    /**
     * 修改保证金
     * add by wuxw
     * @param accountBondPo
     * @return
     */
    ResponseEntity<String> delete(AccountBondPo accountBondPo);


}
