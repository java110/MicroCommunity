package com.java110.acct.bmo.accountWithdrawalApply;
import com.java110.dto.accountWithdrawalApply.AccountWithdrawalApplyDto;
import org.springframework.http.ResponseEntity;
public interface IGetAccountWithdrawalApplyBMO {


    /**
     * 查询账户提现
     * add by wuxw
     * @param  accountWithdrawalApplyDto
     * @return
     */
    ResponseEntity<String> get(AccountWithdrawalApplyDto accountWithdrawalApplyDto);


}
