package com.java110.acct.bmo.account;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.accountDetail.AccountDetailPo;
import org.springframework.http.ResponseEntity;

public interface IOwnerPrestoreAccountBMO {


    /**
     * 业主预存
     * add by wuxw
     * @param  accountDetailPo
     * @return
     */
    ResponseEntity<String> prestore(AccountDetailPo accountDetailPo, JSONObject reqJson);


}
