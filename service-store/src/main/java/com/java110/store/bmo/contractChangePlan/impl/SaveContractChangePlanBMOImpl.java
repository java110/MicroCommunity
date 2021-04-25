package com.java110.store.bmo.contractChangePlan.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contractChangePlan.ContractChangePlanDto;
import com.java110.dto.contractChangePlanRoom.ContractChangePlanRoomDto;
import com.java110.dto.contractRoom.ContractRoomDto;
import com.java110.intf.common.IContractChangeUserInnerServiceSMO;
import com.java110.intf.store.*;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import com.java110.po.contractChangePlanRoom.ContractChangePlanRoomPo;
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

    @Autowired
    private IContractChangeUserInnerServiceSMO contractChangeUserInnerServiceSMO;

    @Autowired
    private IContractChangePlanRoomInnerServiceSMO contractChangePlanRoomInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractChangePlanPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ContractChangePlanPo contractChangePlanPo,
                                       ContractChangePlanDetailPo contractChangePlanDetailPo,
                                       List<ContractChangePlanRoomPo> contractChangePlanRoomPos) {

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
        flag = contractChangePlanDetailInnerServiceSMOImpl.saveContractChangePlanDetail(oldContractChangePlanDetailPo);
        if (flag < 1) {
            throw new IllegalArgumentException("保存变更记录失败");
        }

        dealContractChangePlanRooms(contractChangePlanPo,contractChangePlanRoomPos);



        //提交流程
        ContractChangePlanDto contractChangePlanDto = BeanConvertUtil.covertBean(contractChangePlanPo, ContractChangePlanDto.class);
        contractChangePlanDto.setCurrentUserId(contractChangePlanPo.getChangePerson());
        contractChangeUserInnerServiceSMO.startProcess(contractChangePlanDto);

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

    /**
     * 处理合同变更资产功能
     *
     * @param contractChangePlanRoomPos
     */
    private void dealContractChangePlanRooms(ContractChangePlanPo contractChangePlanPo,
                                             List<ContractChangePlanRoomPo> contractChangePlanRoomPos) {

        if (contractChangePlanRoomPos == null || contractChangePlanRoomPos.size() == 0) {
            return;
        }
        ContractChangePlanRoomPo tmpContractChangePlanRoomPo = null;
        for(ContractChangePlanRoomPo contractChangePlanRoomPo : contractChangePlanRoomPos){

            ContractRoomDto contractRoomDto = new ContractRoomDto();
            contractRoomDto.setContractId(contractChangePlanPo.getContractId());
            contractRoomDto.setRoomId(contractChangePlanRoomPo.getRoomId());
            List<ContractRoomDto> contractRoomDtos
                    = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);

            if(contractRoomDtos != null && contractRoomDtos.size()> 0){
                tmpContractChangePlanRoomPo = new ContractChangePlanRoomPo();
                tmpContractChangePlanRoomPo.setContractId(contractChangePlanPo.getContractId());
                tmpContractChangePlanRoomPo.setOwnerId(contractRoomDtos.get(0).getOwnerId());
                tmpContractChangePlanRoomPo.setOwnerName(contractRoomDtos.get(0).getOwnerName());
                tmpContractChangePlanRoomPo.setPlanId(contractChangePlanPo.getPlanId());
                tmpContractChangePlanRoomPo.setPrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_prId));
                tmpContractChangePlanRoomPo.setRoomId(contractRoomDtos.get(0).getRoomId());
                tmpContractChangePlanRoomPo.setRoomName(contractRoomDtos.get(0).getRoomName());
                tmpContractChangePlanRoomPo.setStoreId(contractRoomDtos.get(0).getStoreId());
                tmpContractChangePlanRoomPo.setOperate("DEL");
                contractChangePlanRoomInnerServiceSMOImpl.saveContractChangePlanRoom(tmpContractChangePlanRoomPo);
            }

            contractChangePlanRoomPo.setContractId(contractChangePlanPo.getContractId());
            contractChangePlanRoomPo.setPlanId(contractChangePlanPo.getPlanId());
            contractChangePlanRoomPo.setPrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_prId));
            contractChangePlanRoomPo.setStoreId(contractRoomDtos.get(0).getStoreId());
            contractChangePlanRoomPo.setOperate("ADD");
            contractChangePlanRoomInnerServiceSMOImpl.saveContractChangePlanRoom(contractChangePlanRoomPo);
        }
    }

}
