package com.java110.acct.bmo.accountBond.impl;


import com.java110.acct.bmo.accountBond.IUpdateAccountBondBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.acct.IAccountBondInnerServiceSMO;
import com.java110.po.accountBond.AccountBondPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAccountBondBMOImpl")
public class UpdateAccountBondBMOImpl implements IUpdateAccountBondBMO {

    @Autowired
    private IAccountBondInnerServiceSMO accountBondInnerServiceSMOImpl;

    /**
     *
     *
     * @param accountBondPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(AccountBondPo accountBondPo) {

        int flag = accountBondInnerServiceSMOImpl.updateAccountBond(accountBondPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
