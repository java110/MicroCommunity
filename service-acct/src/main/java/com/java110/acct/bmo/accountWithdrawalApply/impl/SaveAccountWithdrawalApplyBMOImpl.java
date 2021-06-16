package com.java110.acct.bmo.accountWithdrawalApply.impl;

import com.java110.acct.bmo.accountWithdrawalApply.ISaveAccountWithdrawalApplyBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;

import com.java110.intf.acct.IAccountWithdrawalApplyInnerServiceSMO;
import com.java110.po.accountWithdrawalApply.AccountWithdrawalApplyPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("saveAccountWithdrawalApplyBMOImpl")
public class SaveAccountWithdrawalApplyBMOImpl implements ISaveAccountWithdrawalApplyBMO {

    @Autowired
    private IAccountWithdrawalApplyInnerServiceSMO accountWithdrawalApplyInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param accountWithdrawalApplyPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AccountWithdrawalApplyPo accountWithdrawalApplyPo) {

        accountWithdrawalApplyPo.setApplyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));
        int flag = accountWithdrawalApplyInnerServiceSMOImpl.saveAccountWithdrawalApply(accountWithdrawalApplyPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
