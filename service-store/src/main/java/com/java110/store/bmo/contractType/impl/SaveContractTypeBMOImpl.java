package com.java110.store.bmo.contractType.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IContractTypeInnerServiceSMO;
import com.java110.po.contractType.ContractTypePo;
import com.java110.store.bmo.contractType.ISaveContractTypeBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractTypeBMOImpl")
public class SaveContractTypeBMOImpl implements ISaveContractTypeBMO {

    @Autowired
    private IContractTypeInnerServiceSMO contractTypeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractTypePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ContractTypePo contractTypePo) {

        contractTypePo.setContractTypeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_contractTypeId));
        int flag = contractTypeInnerServiceSMOImpl.saveContractType(contractTypePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
