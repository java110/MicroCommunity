package com.java110.store.bmo.contractTypeSpec.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IContractTypeSpecInnerServiceSMO;
import com.java110.po.contractTypeSpec.ContractTypeSpecPo;
import com.java110.store.bmo.contractTypeSpec.ISaveContractTypeSpecBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractTypeSpecBMOImpl")
public class SaveContractTypeSpecBMOImpl implements ISaveContractTypeSpecBMO {

    @Autowired
    private IContractTypeSpecInnerServiceSMO contractTypeSpecInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractTypeSpecPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ContractTypeSpecPo contractTypeSpecPo) {

        contractTypeSpecPo.setSpecCd(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_specCd));
        int flag = contractTypeSpecInnerServiceSMOImpl.saveContractTypeSpec(contractTypeSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
