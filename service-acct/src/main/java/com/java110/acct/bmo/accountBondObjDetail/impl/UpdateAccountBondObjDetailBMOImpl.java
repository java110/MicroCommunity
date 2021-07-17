package com.java110.acct.bmo.accountBondObjDetail.impl;

import com.java110.acct.bmo.accountBondObjDetail.IUpdateAccountBondObjDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.acct.IAccountBondObjDetailInnerServiceSMO;
import com.java110.po.accountBondObjDetail.AccountBondObjDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAccountBondObjDetailBMOImpl")
public class UpdateAccountBondObjDetailBMOImpl implements IUpdateAccountBondObjDetailBMO {

    @Autowired
    private IAccountBondObjDetailInnerServiceSMO accountBondObjDetailInnerServiceSMOImpl;

    /**
     *
     *
     * @param accountBondObjDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(AccountBondObjDetailPo accountBondObjDetailPo) {

        int flag = accountBondObjDetailInnerServiceSMOImpl.updateAccountBondObjDetail(accountBondObjDetailPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
