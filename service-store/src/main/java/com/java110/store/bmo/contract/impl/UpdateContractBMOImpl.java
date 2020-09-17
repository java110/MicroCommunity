package com.java110.store.bmo.contract.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.po.contract.ContractPo;
import com.java110.store.bmo.contract.IUpdateContractBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractBMOImpl")
public class UpdateContractBMOImpl implements IUpdateContractBMO {

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    /**
     * @param contractPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractPo contractPo) {

        int flag = contractInnerServiceSMOImpl.updateContract(contractPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
