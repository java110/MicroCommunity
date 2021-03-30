package com.java110.store.bmo.contractChangePlan.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractChangePlanInnerServiceSMO;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import com.java110.store.bmo.contractChangePlan.IUpdateContractChangePlanBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractChangePlanBMOImpl")
public class UpdateContractChangePlanBMOImpl implements IUpdateContractChangePlanBMO {

    @Autowired
    private IContractChangePlanInnerServiceSMO contractChangePlanInnerServiceSMOImpl;

    /**
     * @param contractChangePlanPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractChangePlanPo contractChangePlanPo) {

        int flag = contractChangePlanInnerServiceSMOImpl.updateContractChangePlan(contractChangePlanPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
