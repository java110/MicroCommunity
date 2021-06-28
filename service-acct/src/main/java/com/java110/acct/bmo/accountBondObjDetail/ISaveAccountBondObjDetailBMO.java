package com.java110.acct.bmo.accountBondObjDetail;

import com.java110.po.accountBondObjDetail.AccountBondObjDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAccountBondObjDetailBMO {


    /**
     * 添加保证金明细
     * add by wuxw
     * @param accountBondObjDetailPo
     * @return
     */
    ResponseEntity<String> save(AccountBondObjDetailPo accountBondObjDetailPo);


}
