package com.java110.acct.bmo.accountWithdrawalApply;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.java110.po.accountWithdrawalApply.AccountWithdrawalApplyPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAccountWithdrawalApplyBMO {


    /**
     * 添加账户提现
     * add by wuxw
     * @param accountWithdrawalApplyPo
     * @return
     */
    ResponseEntity<String> save(AccountWithdrawalApplyPo accountWithdrawalApplyPo, String userId, JSONObject reqJson);


}
