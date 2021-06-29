package com.java110.acct.bmo.accountBondObjDetail.impl;

import com.java110.acct.bmo.accountBondObjDetail.ISaveAccountBondObjDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;

import com.java110.intf.acct.IAccountBondObjDetailInnerServiceSMO;
import com.java110.po.accountBondObjDetail.AccountBondObjDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAccountBondObjDetailBMOImpl")
public class SaveAccountBondObjDetailBMOImpl implements ISaveAccountBondObjDetailBMO {

    @Autowired
    private IAccountBondObjDetailInnerServiceSMO accountBondObjDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param accountBondObjDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AccountBondObjDetailPo accountBondObjDetailPo) {

        accountBondObjDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = accountBondObjDetailInnerServiceSMOImpl.saveAccountBondObjDetail(accountBondObjDetailPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
