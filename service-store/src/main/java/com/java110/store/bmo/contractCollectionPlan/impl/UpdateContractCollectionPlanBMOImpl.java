package com.java110.store.bmo.contractCollectionPlan.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractCollectionPlanInnerServiceSMO;
import com.java110.po.contractCollectionPlan.ContractCollectionPlanPo;
import com.java110.store.bmo.contractCollectionPlan.IUpdateContractCollectionPlanBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractCollectionPlanBMOImpl")
public class UpdateContractCollectionPlanBMOImpl implements IUpdateContractCollectionPlanBMO {

    @Autowired
    private IContractCollectionPlanInnerServiceSMO contractCollectionPlanInnerServiceSMOImpl;

    /**
     * @param contractCollectionPlanPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractCollectionPlanPo contractCollectionPlanPo) {

        int flag = contractCollectionPlanInnerServiceSMOImpl.updateContractCollectionPlan(contractCollectionPlanPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
