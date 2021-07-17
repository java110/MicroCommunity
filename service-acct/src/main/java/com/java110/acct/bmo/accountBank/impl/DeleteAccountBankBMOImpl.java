package com.java110.acct.bmo.accountBank.impl;

import com.java110.acct.bmo.accountBank.IDeleteAccountBankBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.acct.IAccountBankInnerServiceSMO;
import com.java110.po.accountBank.AccountBankPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAccountBankBMOImpl")
public class DeleteAccountBankBMOImpl implements IDeleteAccountBankBMO {

    @Autowired
    private IAccountBankInnerServiceSMO accountBankInnerServiceSMOImpl;

    /**
     * @param accountBankPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(AccountBankPo accountBankPo) {

        int flag = accountBankInnerServiceSMOImpl.deleteAccountBank(accountBankPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
