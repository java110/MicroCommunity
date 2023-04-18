package com.java110.acct.bmo.accountBondObj;
import com.java110.dto.account.AccountBondObjDto;
import org.springframework.http.ResponseEntity;
public interface IGetAccountBondObjBMO {


    /**
     * 查询保证金对象
     * add by wuxw
     * @param  accountBondObjDto
     * @return
     */
    ResponseEntity<String> get(AccountBondObjDto accountBondObjDto);


}
