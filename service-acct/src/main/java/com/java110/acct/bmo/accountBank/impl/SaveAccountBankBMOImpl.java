package com.java110.acct.bmo.accountBank.impl;

import com.java110.acct.bmo.accountBank.ISaveAccountBankBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;

import com.java110.intf.acct.IAccountBankInnerServiceSMO;
import com.java110.po.accountBank.AccountBankPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAccountBankBMOImpl")
public class SaveAccountBankBMOImpl implements ISaveAccountBankBMO {

    @Autowired
    private IAccountBankInnerServiceSMO accountBankInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param accountBankPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AccountBankPo accountBankPo) {

        accountBankPo.setBankId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_bankId));
        int flag = accountBankInnerServiceSMOImpl.saveAccountBank(accountBankPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
