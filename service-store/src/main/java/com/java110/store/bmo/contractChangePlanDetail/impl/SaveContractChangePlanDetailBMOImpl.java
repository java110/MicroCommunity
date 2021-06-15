package com.java110.store.bmo.contractChangePlanDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IContractChangePlanDetailInnerServiceSMO;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import com.java110.store.bmo.contractChangePlanDetail.ISaveContractChangePlanDetailBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractChangePlanDetailBMOImpl")
public class SaveContractChangePlanDetailBMOImpl implements ISaveContractChangePlanDetailBMO {

    @Autowired
    private IContractChangePlanDetailInnerServiceSMO contractChangePlanDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractChangePlanDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ContractChangePlanDetailPo contractChangePlanDetailPo) {

        contractChangePlanDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = contractChangePlanDetailInnerServiceSMOImpl.saveContractChangePlanDetail(contractChangePlanDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
