package com.java110.store.bmo.contractChangePlan.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contract.ContractChangePlanDto;
import com.java110.dto.contract.ContractChangePlanDetailDto;
import com.java110.dto.contract.ContractChangePlanRoomDto;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.contract.ContractTypeDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.common.IContractChangeUserInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.store.*;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.contract.ContractPo;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import com.java110.po.contractChangePlanRoom.ContractChangePlanRoomPo;
import com.java110.po.contractRoom.ContractRoomPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.room.RoomPo;
import com.java110.store.bmo.contractChangePlan.ISaveContractChangePlanBMO;
import com.java110.utils.constant.StatusConstant;
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

    @Autowired
    private IContractTypeInnerServiceSMO contractTypeInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractChangePlanPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ContractChangePlanPo contractChangePlanPo,
                                       ContractChangePlanDetailPo contractChangePlanDetailPo,
                                       List<ContractChangePlanRoomPo> contractChangePlanRoomPos,
                                       JSONObject reqJson) {

        //查询老的合同信息
        ContractDto contractDto = new ContractDto();
        contractDto.setContractId(contractChangePlanDetailPo.getContractId());
        contractDto.setStoreId(contractChangePlanDetailPo.getStoreId());
        List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        Assert.listOnlyOne(contractDtos, "合同不存在");

        //查询 合同是否需要审核
        ContractTypeDto contractTypeDto = new ContractTypeDto();
        contractTypeDto.setContractTypeId(contractDtos.get(0).getContractType());
        contractTypeDto.setStoreId(contractDtos.get(0).getStoreId());
        List<ContractTypeDto> contractTypeDtos = contractTypeInnerServiceSMOImpl.queryContractTypes(contractTypeDto);

        Assert.listOnlyOne(contractTypeDtos, "合同类型不存在");

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

        dealContractChangePlanRooms(contractChangePlanPo, contractChangePlanRoomPos);

        //需要审核时才写 工作流
        if (!ContractTypeDto.NO_AUDIT.equals(contractTypeDtos.get(0).getAudit())) {
            //提交流程
            ContractChangePlanDto contractChangePlanDto = BeanConvertUtil.covertBean(contractChangePlanPo, ContractChangePlanDto.class);
            contractChangePlanDto.setCurrentUserId(contractChangePlanPo.getChangePerson());
            contractChangePlanDto.setNextUserId(reqJson.getString("nextUserId"));
            contractChangeUserInnerServiceSMO.startProcess(contractChangePlanDto);
        } else {
            ContractChangePlanPo tmpContractChangePlanPo = new ContractChangePlanPo();
            tmpContractChangePlanPo.setPlanId(contractChangePlanPo.getPlanId());
            tmpContractChangePlanPo.setState(ContractDto.STATE_AUDIT_FINISH);
            tmpContractChangePlanPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
            contractChangePlanInnerServiceSMOImpl.updateContractChangePlan(tmpContractChangePlanPo);
            //修改合同信息
            ContractChangePlanDetailDto contractChangePlanDetailDto = new ContractChangePlanDetailDto();
            contractChangePlanDetailDto.setPlanId(contractChangePlanPo.getPlanId());
            contractChangePlanDetailDto.setStoreId(contractChangePlanPo.getStoreId());
            contractChangePlanDetailDto.setOperate("ADD");
            List<ContractChangePlanDetailDto> contractChangePlanDetailDtos =
                    contractChangePlanDetailInnerServiceSMOImpl.queryContractChangePlanDetails(contractChangePlanDetailDto);

            Assert.listOnlyOne(contractChangePlanDetailDtos, "数据错误");
            ContractPo contractPo = BeanConvertUtil.covertBean(contractChangePlanDetailDtos.get(0), ContractPo.class);

            contractInnerServiceSMOImpl.updateContract(contractPo);
            dealContractChangePlanRoom(tmpContractChangePlanPo, contractDtos.get(0));
        }


        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

    private void dealContractChangePlanRoom(ContractChangePlanPo contractChangePlanPo, ContractDto contractDto) {

        //查询合同房屋
        ContractRoomDto contractRoomDto = new ContractRoomDto();
        contractRoomDto.setStoreId(contractChangePlanPo.getStoreId());
        contractRoomDto.setContractId(contractDto.getContractId());
        List<ContractRoomDto> oldContractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);
        // 查询 是否有资产变更

        ContractChangePlanRoomDto contractChangePlanRoomDto = new ContractChangePlanRoomDto();
        contractChangePlanRoomDto.setPlanId(contractChangePlanPo.getPlanId());
        contractChangePlanRoomDto.setStoreId(contractChangePlanPo.getStoreId());
        contractChangePlanRoomDto.setOperate("ADD");

        List<ContractChangePlanRoomDto> contractChangePlanRoomDtos
                = contractChangePlanRoomInnerServiceSMOImpl.queryContractChangePlanRooms(contractChangePlanRoomDto);

        if (contractChangePlanRoomDtos == null || contractChangePlanRoomDtos.size() < 1) {
            //删除老的关系值
            doDelOldRoomRel(contractChangePlanRoomDtos, oldContractRoomDtos);
            return;
        }
        doAddRoomRel(contractDto, oldContractRoomDtos, contractChangePlanRoomDtos);

        //删除老的关系值
        doDelOldRoomRel(contractChangePlanRoomDtos, oldContractRoomDtos);


    }

    private void doAddRoomRel(ContractDto contractDto, List<ContractRoomDto> oldContractRoomDtos, List<ContractChangePlanRoomDto> contractChangePlanRoomDtos) {
        //删除之前数据 插入新数据
        ContractRoomPo contractRoomPo = null;
        //插入新的关系值
        for (ContractChangePlanRoomDto tmpContractChangePlanRoomDto : contractChangePlanRoomDtos) {
            if (isOldRoom(tmpContractChangePlanRoomDto, oldContractRoomDtos)) {
                continue;
            }
            contractRoomPo = new ContractRoomPo();
            contractRoomPo.setContractId(contractChangePlanRoomDtos.get(0).getContractId());
            contractRoomPo.setStoreId(contractChangePlanRoomDtos.get(0).getStoreId());
            contractRoomPo.setCrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_crId));
            contractRoomPo.setOwnerId(tmpContractChangePlanRoomDto.getOwnerId());
            contractRoomPo.setOwnerName(tmpContractChangePlanRoomDto.getOwnerName());
            contractRoomPo.setRoomId(tmpContractChangePlanRoomDto.getRoomId());
            contractRoomPo.setRoomName(tmpContractChangePlanRoomDto.getRoomName());
            contractRoomInnerServiceSMOImpl.saveContractRoom(contractRoomPo);

            //刷业主
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(contractRoomPo.getRoomId());
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

            if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() > 0) { // 说明业主没有发生变化，后续工作不做处理
                if (contractDto.getObjId().equals(ownerRoomRelDtos.get(0).getOwnerId())) {
                    continue;
                }
            }

            //绑定关系
            String relId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId);
            OwnerRoomRelPo ownerRoomRelPo = new OwnerRoomRelPo();
            ownerRoomRelPo.setEndTime(contractDto.getEndTime());
            ownerRoomRelPo.setStartTime(contractDto.getStartTime());
            ownerRoomRelPo.setOwnerId(contractDto.getObjId());
            ownerRoomRelPo.setRelId(relId);
            ownerRoomRelPo.setRemark("签订合同自动绑定");
            ownerRoomRelPo.setRoomId(contractRoomPo.getRoomId());
            ownerRoomRelPo.setState("2001");
            ownerRoomRelPo.setUserId("-1");
            ownerRoomRelPo.setOperate("ADD");
            ownerRoomRelPo.setbId("-1");
            ownerRoomRelInnerServiceSMOImpl.saveBusinessOwnerRoomRels(ownerRoomRelPo);

            ownerRoomRelPo = new OwnerRoomRelPo();
            ownerRoomRelPo.setEndTime(contractDto.getEndTime());
            ownerRoomRelPo.setStartTime(contractDto.getStartTime());
            ownerRoomRelPo.setOwnerId(contractDto.getObjId());
            ownerRoomRelPo.setRelId(relId);
            ownerRoomRelPo.setRemark("签订合同自动绑定");
            ownerRoomRelPo.setRoomId(contractRoomPo.getRoomId());
            ownerRoomRelPo.setState("2001");
            ownerRoomRelPo.setUserId("-1");
            ownerRoomRelInnerServiceSMOImpl.saveOwnerRoomRels(ownerRoomRelPo);

            //修改房屋状态
            RoomPo roomPo = new RoomPo();
            roomPo.setRoomId(contractRoomPo.getRoomId());
            roomPo.setState(RoomDto.STATE_SELL);
            roomPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
            roomInnerServiceSMOImpl.updateRooms(roomPo);
            //删除老的
            if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() > 0) {
                ownerRoomRelPo = new OwnerRoomRelPo();
                ownerRoomRelPo.setStatusCd(StatusConstant.STATUS_CD_INVALID);
                ownerRoomRelPo.setRelId(ownerRoomRelDtos.get(0).getRelId());
                ownerRoomRelInnerServiceSMOImpl.updateOwnerRoomRels(ownerRoomRelPo);
                ownerRoomRelPo = BeanConvertUtil.covertBean(ownerRoomRelDtos.get(0), OwnerRoomRelPo.class);
                ownerRoomRelPo.setbId("-1");
                ownerRoomRelPo.setOperate("DEL");
                ownerRoomRelInnerServiceSMOImpl.saveBusinessOwnerRoomRels(ownerRoomRelPo);
            }
        }
    }

    private void doDelOldRoomRel(List<ContractChangePlanRoomDto> contractChangePlanRoomDtos, List<ContractRoomDto> oldContractRoomDtos) {
        ContractRoomPo contractRoomPo;
        OwnerRoomRelPo ownerRoomRelPo = null;
        for (ContractRoomDto oldContractRoomDto : oldContractRoomDtos) {
            if (isDelOldRoom(oldContractRoomDto, contractChangePlanRoomDtos)) {
                continue;
            }
            contractRoomPo = new ContractRoomPo();
            contractRoomPo.setContractId(oldContractRoomDtos.get(0).getContractId());
            contractRoomPo.setStoreId(oldContractRoomDtos.get(0).getStoreId());
            contractRoomPo.setCrId(oldContractRoomDto.getCrId());
            contractRoomInnerServiceSMOImpl.deleteContractRoom(contractRoomPo);

            //刷业主
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(oldContractRoomDto.getRoomId());
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

            if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) { // 说明房屋没有业主
                continue;
            }
            //删除关系
            ownerRoomRelPo = new OwnerRoomRelPo();
            ownerRoomRelPo.setStatusCd(StatusConstant.STATUS_CD_INVALID);
            ownerRoomRelPo.setRelId(ownerRoomRelDtos.get(0).getRelId());
            ownerRoomRelInnerServiceSMOImpl.updateOwnerRoomRels(ownerRoomRelPo);

            //插入删除关系
            ownerRoomRelPo = BeanConvertUtil.covertBean(ownerRoomRelDtos.get(0), OwnerRoomRelPo.class);
            ownerRoomRelPo.setbId("-1");
            ownerRoomRelPo.setOperate("DEL");
            ownerRoomRelInnerServiceSMOImpl.saveBusinessOwnerRoomRels(ownerRoomRelPo);


            //修改房屋状态
            RoomPo roomPo = new RoomPo();
            roomPo.setRoomId(oldContractRoomDto.getRoomId());
            roomPo.setState(RoomDto.STATE_FREE);
            roomPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
            roomInnerServiceSMOImpl.updateRooms(roomPo);
        }
    }

    private boolean isDelOldRoom(ContractRoomDto oldContractRoomDto, List<ContractChangePlanRoomDto> contractChangePlanRoomDtos) {
        if (contractChangePlanRoomDtos == null || contractChangePlanRoomDtos.size() > 0) {
            return false;
        }

        for (ContractChangePlanRoomDto contractChangePlanRoomDto : contractChangePlanRoomDtos) {
            if (contractChangePlanRoomDto.getRoomId().equals(oldContractRoomDto.getRoomId())) {
                return true;
            }
        }

        return false;
    }

    private boolean isOldRoom(ContractChangePlanRoomDto tmpContractChangePlanRoomDto, List<ContractRoomDto> oldContractRoomDtos) {
        if (oldContractRoomDtos == null || oldContractRoomDtos.size() > 0) {
            return false;
        }

        for (ContractRoomDto contractRoomDto : oldContractRoomDtos) {
            if (tmpContractChangePlanRoomDto.getRoomId().equals(contractRoomDto.getRoomId())) {
                return true;
            }
        }

        return false;
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
        for (ContractChangePlanRoomPo contractChangePlanRoomPo : contractChangePlanRoomPos) {

            ContractRoomDto contractRoomDto = new ContractRoomDto();
            contractRoomDto.setContractId(contractChangePlanPo.getContractId());
            contractRoomDto.setRoomId(contractChangePlanRoomPo.getRoomId());
            List<ContractRoomDto> contractRoomDtos
                    = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);

            if (contractRoomDtos != null && contractRoomDtos.size() > 0) {
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
            contractChangePlanRoomPo.setStoreId(contractChangePlanPo.getStoreId());
            contractChangePlanRoomPo.setOperate("ADD");
            contractChangePlanRoomInnerServiceSMOImpl.saveContractChangePlanRoom(contractChangePlanRoomPo);
        }
    }

}
