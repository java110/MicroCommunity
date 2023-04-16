package com.java110.acct.bmo.account;
import com.java110.dto.account.AccountDto;
import com.java110.dto.account.AccountDetailDto;
import com.java110.dto.owner.OwnerDto;
import org.springframework.http.ResponseEntity;

public interface IGetAccountBMO {


    /**
     * 查询账户信息
     * add by wuxw
     * @param  accountDto
     * @return
     */
    ResponseEntity<String> get(AccountDto accountDto);


    /**
     * 查询账户交易明细
     * @param accountDto
     * @return
     */
    ResponseEntity<String> getDetail(AccountDetailDto accountDto);

    /**
     * 查询业主账户
     * @param accountDto
     * @param ownerDto
     * @return
     */
    ResponseEntity<String> queryOwnerAccount(AccountDto accountDto, OwnerDto ownerDto);
}
