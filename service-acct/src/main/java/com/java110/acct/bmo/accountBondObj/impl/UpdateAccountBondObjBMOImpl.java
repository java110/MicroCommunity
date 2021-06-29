package com.java110.acct.bmo.accountBondObj.impl;

import com.java110.acct.bmo.accountBondObj.IUpdateAccountBondObjBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.acct.IAccountBondObjInnerServiceSMO;
import com.java110.po.accountBondObj.AccountBondObjPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("updateAccountBondObjBMOImpl")
public class UpdateAccountBondObjBMOImpl implements IUpdateAccountBondObjBMO {

    @Autowired
    private IAccountBondObjInnerServiceSMO accountBondObjInnerServiceSMOImpl;

    /**
     *
     *
     * @param accountBondObjPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(AccountBondObjPo accountBondObjPo) {

        int flag = accountBondObjInnerServiceSMOImpl.updateAccountBondObj(accountBondObjPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
