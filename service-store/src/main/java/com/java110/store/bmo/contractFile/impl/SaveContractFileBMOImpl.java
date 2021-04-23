package com.java110.store.bmo.contractFile.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IContractFileInnerServiceSMO;
import com.java110.po.contractFile.ContractFilePo;
import com.java110.store.bmo.contractFile.ISaveContractFileBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractFileBMOImpl")
public class SaveContractFileBMOImpl implements ISaveContractFileBMO {

    @Autowired
    private IContractFileInnerServiceSMO contractFileInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractFilePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ContractFilePo contractFilePo) {

        contractFilePo.setContractFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_contractFileId));
        int flag = contractFileInnerServiceSMOImpl.saveContractFile(contractFilePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
