package com.java110.store.bmo.contractChangePlanDetailAttr.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractChangePlanDetailAttrInnerServiceSMO;
import com.java110.po.contractChangePlanDetailAttr.ContractChangePlanDetailAttrPo;
import com.java110.store.bmo.contractChangePlanDetailAttr.IUpdateContractChangePlanDetailAttrBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractChangePlanDetailAttrBMOImpl")
public class UpdateContractChangePlanDetailAttrBMOImpl implements IUpdateContractChangePlanDetailAttrBMO {

    @Autowired
    private IContractChangePlanDetailAttrInnerServiceSMO contractChangePlanDetailAttrInnerServiceSMOImpl;

    /**
     *
     *
     * @param contractChangePlanDetailAttrPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo) {

        int flag = contractChangePlanDetailAttrInnerServiceSMOImpl.updateContractChangePlanDetailAttr(contractChangePlanDetailAttrPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
