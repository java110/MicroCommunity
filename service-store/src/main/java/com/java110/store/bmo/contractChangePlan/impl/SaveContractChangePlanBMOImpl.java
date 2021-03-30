package com.java110.store.bmo.contractChangePlan.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contract.ContractDto;
import com.java110.intf.store.IContractChangePlanDetailInnerServiceSMO;
import com.java110.intf.store.IContractChangePlanInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import com.java110.store.bmo.contractChangePlan.ISaveContractChangePlanBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveContractChangePlanBMOImpl")
public class SaveContractChangePlanBMOImpl implements ISaveContractChangePlanBMO {

    @Autowired
    private IContractChangePlanInnerServiceSMO contractChangePlanInnerServiceSMOImpl;

    @Autowired
    private IContractChangePlanDetailInnerServiceSMO contractChangePlanDetailInnerServiceSMOImpl;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractChangePlanPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ContractChangePlanPo contractChangePlanPo, ContractChangePlanDetailPo contractChangePlanDetailPo) {

        //查询老的合同信息
        ContractDto contractDto = new ContractDto();
        contractDto.setContractId(contractChangePlanDetailPo.getContractId());
        contractDto.setStoreId(contractChangePlanDetailPo.getStoreId());
        List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        Assert.listOnlyOne(contractDtos, "合同不存在");

        contractChangePlanPo.setPlanId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_planId));
        int flag = contractChangePlanInnerServiceSMOImpl.saveContractChangePlan(contractChangePlanPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }
        //插入新值
        contractChangePlanDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        contractChangePlanDetailPo.setPlanId(contractChangePlanPo.getPlanId());
        contractChangePlanDetailPo.setOperate("ADD");
        flag = contractChangePlanDetailInnerServiceSMOImpl.saveContractChangePlanDetail(contractChangePlanDetailPo);
        if (flag < 1) {
            throw new IllegalArgumentException("保存变更记录失败");
        }

        ContractChangePlanDetailPo oldContractChangePlanDetailPo = BeanConvertUtil.covertBean(contractDtos.get(0), ContractChangePlanDetailPo.class);
        oldContractChangePlanDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        oldContractChangePlanDetailPo.setPlanId(contractChangePlanPo.getPlanId());
        oldContractChangePlanDetailPo.setOperate("DEL");
        flag = contractChangePlanDetailInnerServiceSMOImpl.saveContractChangePlanDetail(contractChangePlanDetailPo);
        if (flag < 1) {
            throw new IllegalArgumentException("保存变更记录失败");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
