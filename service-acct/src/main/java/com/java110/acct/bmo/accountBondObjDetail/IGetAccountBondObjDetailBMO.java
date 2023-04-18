package com.java110.acct.bmo.accountBondObjDetail;
import com.java110.dto.account.AccountBondObjDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetAccountBondObjDetailBMO {


    /**
     * 查询保证金明细
     * add by wuxw
     * @param  accountBondObjDetailDto
     * @return
     */
    ResponseEntity<String> get(AccountBondObjDetailDto accountBondObjDetailDto);


}
