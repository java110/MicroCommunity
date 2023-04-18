package com.java110.acct.bmo.accountBond;
import com.java110.dto.account.AccountBondDto;
import org.springframework.http.ResponseEntity;
public interface IGetAccountBondBMO {


    /**
     * 查询保证金
     * add by wuxw
     * @param  accountBondDto
     * @return
     */
    ResponseEntity<String> get(AccountBondDto accountBondDto);


}
