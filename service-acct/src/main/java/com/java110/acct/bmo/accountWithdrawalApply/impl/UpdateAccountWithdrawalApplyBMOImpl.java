package com.java110.acct.bmo.accountWithdrawalApply.impl;

import com.java110.acct.bmo.accountWithdrawalApply.IUpdateAccountWithdrawalApplyBMO;
import com.java110.core.annotation.Java110Transactional;

import com.java110.intf.acct.IAccountWithdrawalApplyInnerServiceSMO;
import com.java110.po.accountWithdrawalApply.AccountWithdrawalApplyPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("updateAccountWithdrawalApplyBMOImpl")
public class UpdateAccountWithdrawalApplyBMOImpl implements IUpdateAccountWithdrawalApplyBMO {

    @Autowired
    private IAccountWithdrawalApplyInnerServiceSMO accountWithdrawalApplyInnerServiceSMOImpl;

    /**
     *
     *
     * @param accountWithdrawalApplyPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(AccountWithdrawalApplyPo accountWithdrawalApplyPo) {

        int flag = accountWithdrawalApplyInnerServiceSMOImpl.updateAccountWithdrawalApply(accountWithdrawalApplyPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
