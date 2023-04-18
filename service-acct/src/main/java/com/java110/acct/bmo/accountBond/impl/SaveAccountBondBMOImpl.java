package com.java110.acct.bmo.accountBond.impl;

import com.java110.acct.bmo.accountBond.ISaveAccountBondBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountBondDto;
import com.java110.intf.acct.IAccountBondInnerServiceSMO;
import com.java110.po.accountBond.AccountBondPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAccountBondBMOImpl")
public class SaveAccountBondBMOImpl implements ISaveAccountBondBMO {

    @Autowired
    private IAccountBondInnerServiceSMO accountBondInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param accountBondPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AccountBondPo accountBondPo) {

        AccountBondDto accountBondDto = new AccountBondDto();
        accountBondDto.setObjId( accountBondPo.getObjId() );
        int listFag= accountBondInnerServiceSMOImpl.queryAccountBondsCount( accountBondDto );
        if (listFag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "一种商铺类型只能设置一种保证金");
        }
        accountBondPo.setBondId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_bondId));
        int flag = accountBondInnerServiceSMOImpl.saveAccountBond(accountBondPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
