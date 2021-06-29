package com.java110.acct.bmo.accountBondObj.impl;

import com.java110.acct.bmo.accountBondObj.ISaveAccountBondObjBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.acct.IAccountBondObjInnerServiceSMO;
import com.java110.po.accountBondObj.AccountBondObjPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAccountBondObjBMOImpl")
public class SaveAccountBondObjBMOImpl implements ISaveAccountBondObjBMO {

    @Autowired
    private IAccountBondObjInnerServiceSMO accountBondObjInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param accountBondObjPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AccountBondObjPo accountBondObjPo) {

        accountBondObjPo.setBobjId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_bobjId));
        int flag = accountBondObjInnerServiceSMOImpl.saveAccountBondObj(accountBondObjPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
