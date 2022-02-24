package com.java110.store.bmo.contractChangePlan.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IContractChangeUserInnerServiceSMO;
import com.java110.intf.store.IContractChangePlanInnerServiceSMO;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import com.java110.store.bmo.contractChangePlan.IDeleteContractChangePlanBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractChangePlanBMOImpl")
public class DeleteContractChangePlanBMOImpl implements IDeleteContractChangePlanBMO {

    @Autowired
    private IContractChangePlanInnerServiceSMO contractChangePlanInnerServiceSMOImpl;

    @Autowired
    private IContractChangeUserInnerServiceSMO contractChangeUserInnerServiceSMO;

    /**
     * @param contractChangePlanPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ContractChangePlanPo contractChangePlanPo) {

        int flag = contractChangePlanInnerServiceSMOImpl.deleteContractChangePlan(contractChangePlanPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        contractChangeUserInnerServiceSMO.deleteTask(contractChangePlanPo);
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

}
