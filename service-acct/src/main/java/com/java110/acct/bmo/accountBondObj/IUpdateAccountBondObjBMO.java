package com.java110.acct.bmo.accountBondObj;
import com.java110.po.account.AccountBondObjPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAccountBondObjBMO {


    /**
     * 修改保证金对象
     * add by wuxw
     * @param accountBondObjPo
     * @return
     */
    ResponseEntity<String> update(AccountBondObjPo accountBondObjPo);


}
