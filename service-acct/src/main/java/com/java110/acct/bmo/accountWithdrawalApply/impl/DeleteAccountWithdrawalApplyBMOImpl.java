package com.java110.acct.bmo.accountWithdrawalApply.impl;

import com.java110.acct.bmo.accountWithdrawalApply.IDeleteAccountWithdrawalApplyBMO;
import com.java110.core.annotation.Java110Transactional;

import com.java110.intf.acct.IAccountWithdrawalApplyInnerServiceSMO;
import com.java110.po.accountWithdrawalApply.AccountWithdrawalApplyPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("deleteAccountWithdrawalApplyBMOImpl")
public class DeleteAccountWithdrawalApplyBMOImpl implements IDeleteAccountWithdrawalApplyBMO {

    @Autowired
    private IAccountWithdrawalApplyInnerServiceSMO accountWithdrawalApplyInnerServiceSMOImpl;

    /**
     * @param accountWithdrawalApplyPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(AccountWithdrawalApplyPo accountWithdrawalApplyPo) {

        int flag = accountWithdrawalApplyInnerServiceSMOImpl.deleteAccountWithdrawalApply(accountWithdrawalApplyPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
