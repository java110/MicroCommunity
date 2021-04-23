package com.java110.store.bmo.contractChangePlanDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractChangePlanDetailInnerServiceSMO;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import com.java110.store.bmo.contractChangePlanDetail.IUpdateContractChangePlanDetailBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractChangePlanDetailBMOImpl")
public class UpdateContractChangePlanDetailBMOImpl implements IUpdateContractChangePlanDetailBMO {

    @Autowired
    private IContractChangePlanDetailInnerServiceSMO contractChangePlanDetailInnerServiceSMOImpl;

    /**
     * @param contractChangePlanDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractChangePlanDetailPo contractChangePlanDetailPo) {

        int flag = contractChangePlanDetailInnerServiceSMOImpl.updateContractChangePlanDetail(contractChangePlanDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
