package com.java110.store.bmo.contractAttr.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
import com.java110.po.contractAttr.ContractAttrPo;
import com.java110.store.bmo.contractAttr.ISaveContractAttrBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractAttrBMOImpl")
public class SaveContractAttrBMOImpl implements ISaveContractAttrBMO {

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractAttrPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ContractAttrPo contractAttrPo) {

        contractAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        int flag = contractAttrInnerServiceSMOImpl.saveContractAttr(contractAttrPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
