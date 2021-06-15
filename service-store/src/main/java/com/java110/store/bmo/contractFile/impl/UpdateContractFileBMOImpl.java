package com.java110.store.bmo.contractFile.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractFileInnerServiceSMO;
import com.java110.po.contractFile.ContractFilePo;
import com.java110.store.bmo.contractFile.IUpdateContractFileBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractFileBMOImpl")
public class UpdateContractFileBMOImpl implements IUpdateContractFileBMO {

    @Autowired
    private IContractFileInnerServiceSMO contractFileInnerServiceSMOImpl;

    /**
     *
     *
     * @param contractFilePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractFilePo contractFilePo) {

        int flag = contractFileInnerServiceSMOImpl.updateContractFile(contractFilePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
