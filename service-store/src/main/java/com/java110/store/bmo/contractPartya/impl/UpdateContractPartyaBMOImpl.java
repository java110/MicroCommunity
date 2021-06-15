package com.java110.store.bmo.contractPartya.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractPartyaInnerServiceSMO;
import com.java110.po.contractPartya.ContractPartyaPo;
import com.java110.store.bmo.contractPartya.IUpdateContractPartyaBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractPartyaBMOImpl")
public class UpdateContractPartyaBMOImpl implements IUpdateContractPartyaBMO {

    @Autowired
    private IContractPartyaInnerServiceSMO contractPartyaInnerServiceSMOImpl;

    /**
     * @param contractPartyaPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractPartyaPo contractPartyaPo) {

        int flag = contractPartyaInnerServiceSMOImpl.updateContractPartya(contractPartyaPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
