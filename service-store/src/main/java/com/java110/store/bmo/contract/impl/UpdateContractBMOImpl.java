package com.java110.store.bmo.contract.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contract.ContractAttrDto;
import com.java110.dto.contract.ContractChangePlanDto;
import com.java110.dto.contract.ContractChangePlanDetailDto;
import com.java110.dto.contract.ContractChangePlanRoomDto;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.common.IContractApplyUserInnerServiceSMO;
import com.java110.intf.common.IContractChangeUserInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
import com.java110.intf.store.IContractChangePlanDetailInnerServiceSMO;
import com.java110.intf.store.IContractChangePlanInnerServiceSMO;
import com.java110.intf.store.IContractChangePlanRoomInnerServiceSMO;
import com.java110.intf.store.IContractFileInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.po.contract.ContractPo;
import com.java110.po.contractAttr.ContractAttrPo;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import com.java110.po.contractFile.ContractFilePo;
import com.java110.po.contractRoom.ContractRoomPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.rentingPool.RentingPoolPo;
import com.java110.po.room.RoomPo;
import com.java110.store.bmo.contract.IUpdateContractBMO;
import com.java110.store.bmo.contractFile.IDeleteContractFileBMO;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("updateContractBMOImpl")
public class UpdateContractBMOImpl implements IUpdateContractBMO {

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;

    @Autowired
    private IContractApplyUserInnerServiceSMO contractApplyUserInnerServiceSMOImpl;

    @Autowired
    private IContractChangeUserInnerServiceSMO contractChangeUserInnerServiceSMOImpl;

    @Autowired
    private IContractChangePlanInnerServiceSMO contractChangePlanInnerServiceSMOImpl;

    @Autowired
    private IContractChangePlanDetailInnerServiceSMO contractChangePlanDetailInnerServiceSMOImpl;

    @Autowired
    private IContractChangePlanRoomInnerServiceSMO contractChangePlanRoomInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;


    @Autowired
    private IContractFileInnerServiceSMO contractFileInnerServiceSMOImpl;

    @Autowired
    private IDeleteContractFileBMO deleteContractFileBMOImpl;


    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    /**
     * @param contractPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractPo contractPo, JSONObject reqJson) {

        int flag = contractInnerServiceSMOImpl.updateContract(contractPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");

        }

        //附件保存--先删除原来所有的附件再进行保存
        ContractFilePo contractFilePo = new ContractFilePo();
        contractFilePo.setContractId(contractPo.getContractId());
        deleteContractFileBMOImpl.delete(contractFilePo);

        List<ContractFilePo> filePos = contractPo.getContractFilePo();
        if (filePos != null) {
            for (ContractFilePo file : filePos) {
                if (file.getFileRealName().length() > 0 && file.getFileSaveName().length() > 0) {
                    file.setContractId(contractPo.getContractId());
                    contractFileInnerServiceSMOImpl.saveContractFile(file);
                }
            }
        }


        noticeRentUpdateState(contractPo);

        if (!reqJson.containsKey("contractTypeSpecs")) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }


        JSONArray contractTypeSpecs = reqJson.getJSONArray("contractTypeSpecs");

        if (contractTypeSpecs.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        for (int typeSpecIndex = 0; typeSpecIndex < contractTypeSpecs.size(); typeSpecIndex++) {
            updateContractAttr(contractTypeSpecs.getJSONObject(typeSpecIndex), contractPo);
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

    @Override
    public ResponseEntity<String> needAuditContract(ContractDto contractDto, JSONObject reqJson) {

        ContractDto tmpContractDto = new ContractDto();
        tmpContractDto.setContractId(contractDto.getContractId());
        tmpContractDto.setStoreId(contractDto.getStoreId());
        List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(tmpContractDto);
        Assert.listOnlyOne(contractDtos, "合同不存在");
        contractDto.setStartUserId(contractDtos.get(0).getStartUserId());
        contractDto.setNextUserId(reqJson.getString("nextUserId"));

        boolean isLastTask = contractApplyUserInnerServiceSMOImpl.completeTask(contractDto);
        if (isLastTask) {
            ContractPo contractPo = new ContractPo();
            contractPo.setContractId(contractDto.getContractId());
            contractPo.setState(ContractDto.STATE_AUDIT_FINISH);
            contractPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
            contractInnerServiceSMOImpl.updateContract(contractPo);
        } else { //修改为审核中
            ContractPo contractPo = new ContractPo();
            contractPo.setContractId(contractDto.getContractId());
            contractPo.setState(ContractDto.STATE_AUDIT_DOING);
            if ("1200".equals(reqJson.getString("state"))) {
                contractPo.setState(ContractDto.STATE_FAIL);
            }
            contractPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
            contractInnerServiceSMOImpl.updateContract(contractPo);
        }
        return ResultVo.success();
    }

    @Override
    public ResponseEntity<String> needAuditContractPlan(ContractChangePlanDto contractChangePlanDto, JSONObject reqJson) {

        //查询老的合同信息
        ContractDto contractDto = new ContractDto();
        contractDto.setContractId(contractChangePlanDto.getContractId());
        contractDto.setStoreId(contractChangePlanDto.getStoreId());
        List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        Assert.listOnlyOne(contractDtos, "合同不存在");

        ContractChangePlanDto tmpContractChangePlanDto = new ContractChangePlanDto();
        tmpContractChangePlanDto.setPlanId(contractChangePlanDto.getPlanId());
        tmpContractChangePlanDto.setStoreId(contractChangePlanDto.getStoreId());
        List<ContractChangePlanDto> contractChangePlanDtos = contractChangePlanInnerServiceSMOImpl.queryContractChangePlans(tmpContractChangePlanDto);
        Assert.listOnlyOne(contractChangePlanDtos, "合同计划不存在");
        contractChangePlanDto.setStartUserId(contractChangePlanDtos.get(0).getChangePerson());
        contractChangePlanDto.setNextUserId(reqJson.getString("nextUserId"));

        boolean isLastTask = contractChangeUserInnerServiceSMOImpl.completeTask(contractChangePlanDto);
        if (isLastTask) {
            ContractChangePlanPo contractChangePlanPo = new ContractChangePlanPo();
            contractChangePlanPo.setPlanId(contractChangePlanDto.getPlanId());
            contractChangePlanPo.setState(ContractDto.STATE_AUDIT_FINISH);
            contractChangePlanPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
            contractChangePlanInnerServiceSMOImpl.updateContractChangePlan(contractChangePlanPo);
            //修改合同信息
            ContractChangePlanDetailDto contractChangePlanDetailDto = new ContractChangePlanDetailDto();
            contractChangePlanDetailDto.setPlanId(contractChangePlanDto.getPlanId());
            contractChangePlanDetailDto.setStoreId(contractChangePlanDto.getStoreId());
            contractChangePlanDetailDto.setOperate("ADD");
            List<ContractChangePlanDetailDto> contractChangePlanDetailDtos =
                    contractChangePlanDetailInnerServiceSMOImpl.queryContractChangePlanDetails(contractChangePlanDetailDto);

            Assert.listOnlyOne(contractChangePlanDetailDtos, "数据错误");
            ContractPo contractPo = BeanConvertUtil.covertBean(contractChangePlanDetailDtos.get(0), ContractPo.class);

            contractInnerServiceSMOImpl.updateContract(contractPo);
            //todo 解决合同bug 只有 资产变更时 操作 合同房屋
            if (ContractChangePlanDto.PLAN_TYPE_CHANGE_ROOM.equals(contractChangePlanDtos.get(0).getPlanType())) {
                dealContractChangePlanRoom(contractChangePlanDto, contractDtos.get(0));
            }

            //todo 如果是租期变更时 将房屋的时间修改为变更后的时间
            if (ContractChangePlanDto.PLAN_TYPE_CHANGE_RENT_DATE.equals(contractChangePlanDtos.get(0).getPlanType())) {
                changeRoomEndTime(contractChangePlanDto, contractDtos.get(0));
            }

        } else { //修改为审核中
            ContractChangePlanPo contractChangePlanPo = new ContractChangePlanPo();
            contractChangePlanPo.setPlanId(contractChangePlanDto.getPlanId());
            contractChangePlanPo.setState(ContractDto.STATE_AUDIT_DOING);
            contractChangePlanPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
            contractChangePlanInnerServiceSMOImpl.updateContractChangePlan(contractChangePlanPo);
        }
        return ResultVo.success();
    }

    /**
     * 修改房屋的租期
     *
     * @param contractChangePlanDto
     * @param contractDto
     */
    private void changeRoomEndTime(ContractChangePlanDto contractChangePlanDto, ContractDto contractDto) {

        //查询合同房屋
        ContractRoomDto contractRoomDto = new ContractRoomDto();
        contractRoomDto.setStoreId(contractChangePlanDto.getStoreId());
        contractRoomDto.setContractId(contractDto.getContractId());
        List<ContractRoomDto> oldContractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);
        if (oldContractRoomDtos == null || oldContractRoomDtos.size() < 1) {
            return;
        }
        Date contractEndDate = DateUtil.getDateFromStringA(contractDto.getEndTime());
        for (ContractRoomDto oldContractRoomDto : oldContractRoomDtos) {
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(oldContractRoomDto.getRoomId());
            ownerRoomRelDto.setOwnerId(contractDto.getObjId());
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

            if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) { // 说明业主没有发生变化，后续工作不做处理
                continue;
            }

            // todo 如果合同的小于房屋的则 不修改
            // todo 这块可以根据实际的一个使用情况看看要不要限制
            Date roomEndDate = ownerRoomRelDtos.get(0).getEndTime();
            if(contractEndDate.getTime() < roomEndDate.getTime()){
                continue;
            }

            //todo 修改时间
            OwnerRoomRelPo ownerRoomRelPo = new OwnerRoomRelPo();
            ownerRoomRelPo.setEndTime(contractDto.getEndTime());
            ownerRoomRelPo.setStartTime(contractDto.getStartTime());
            ownerRoomRelPo.setRelId(ownerRoomRelDtos.get(0).getRelId());
            ownerRoomRelInnerServiceSMOImpl.updateOwnerRoomRels(ownerRoomRelPo);
        }
    }

    private void dealContractChangePlanRoom(ContractChangePlanDto contractChangePlanDto, ContractDto contractDto) {

        //查询合同房屋
        ContractRoomDto contractRoomDto = new ContractRoomDto();
        contractRoomDto.setStoreId(contractChangePlanDto.getStoreId());
        contractRoomDto.setContractId(contractDto.getContractId());
        List<ContractRoomDto> oldContractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);
        // 查询 是否有资产变更

        ContractChangePlanRoomDto contractChangePlanRoomDto = new ContractChangePlanRoomDto();
        contractChangePlanRoomDto.setPlanId(contractChangePlanDto.getPlanId());
        contractChangePlanRoomDto.setStoreId(contractChangePlanDto.getStoreId());
        contractChangePlanRoomDto.setOperate("ADD");

        List<ContractChangePlanRoomDto> contractChangePlanRoomDtos
                = contractChangePlanRoomInnerServiceSMOImpl.queryContractChangePlanRooms(contractChangePlanRoomDto);

        if (contractChangePlanRoomDtos == null || contractChangePlanRoomDtos.size() < 1) {
            //删除老的关系值
            doDelOldRoomRel(contractChangePlanRoomDtos, oldContractRoomDtos);
            return;
        }
        //删除老的关系值
        doDelOldRoomRel(contractChangePlanRoomDtos, oldContractRoomDtos);
        //增加
        doAddRoomRel(contractDto, oldContractRoomDtos, contractChangePlanRoomDtos);


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
        if (contractChangePlanRoomDtos == null || contractChangePlanRoomDtos.size() < 1) {
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
        if (oldContractRoomDtos == null || oldContractRoomDtos.size() < 1) {
            return false;
        }

        for (ContractRoomDto contractRoomDto : oldContractRoomDtos) {
            if (tmpContractChangePlanRoomDto.getRoomId().equals(contractRoomDto.getRoomId())) {
                return true;
            }
        }

        return false;
    }

    private void updateContractAttr(JSONObject jsonObject, ContractPo contractPo) {
        ContractAttrDto contractAttrDto = new ContractAttrDto();
        contractAttrDto.setContractId(contractPo.getContractId());
        contractAttrDto.setSpecCd(jsonObject.getString("specCd"));
        List<ContractAttrDto> contractAttrDtos = contractAttrInnerServiceSMOImpl.queryContractAttrs(contractAttrDto);

        Assert.listOnlyOne(contractAttrDtos, "未找到需要修改的合同属性");
        ContractAttrPo contractAttrPo = new ContractAttrPo();
        contractAttrPo.setAttrId(contractAttrDtos.get(0).getAttrId());
        contractAttrPo.setContractId(contractPo.getContractId());
        contractAttrPo.setSpecCd(jsonObject.getString("specCd"));
        contractAttrPo.setValue(jsonObject.getString("value"));
        contractAttrPo.setStoreId(contractPo.getStoreId());
        int count = contractAttrInnerServiceSMOImpl.updateContractAttr(contractAttrPo);

        if (count < 1) {
            throw new IllegalArgumentException("保存属性失败");
        }
    }

    /**
     * 修改 房屋租赁状态
     *
     * @param contractPo
     */
    private void noticeRentUpdateState(ContractPo contractPo) {

        ContractDto contractDto = new ContractDto();
        contractDto.setContractId(contractPo.getContractId());
        List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        Assert.listOnlyOne(contractDtos, "未找到需要审核合同");

        if (!contractDtos.get(0).getObjType().equals(FeeDto.PAYER_OBJ_TYPE_ROOM)
                || StringUtil.isEmpty(contractDtos.get(0).getObjId())
                || contractDtos.get(0).getObjId().startsWith("-")) {
            return;
        }

        if (!StoreDto.STORE_ADMIN.equals(contractDtos.get(0).getStoreId())) {
            return;
        }
        RentingPoolDto rentingPoolDto = new RentingPoolDto();
        rentingPoolDto.setRoomId(contractDtos.get(0).getObjId());
        rentingPoolDto.setState(RentingPoolDto.STATE_ADMIN_AUDIT);
        List<RentingPoolDto> rentingPoolDtos = rentingPoolInnerServiceSMOImpl.queryRentingPools(rentingPoolDto);

        if (rentingPoolDtos == null || rentingPoolDtos.size() < 1) {
            return;
        }

        RentingPoolPo rentingPoolPo = new RentingPoolPo();
        rentingPoolPo.setCommunityId(rentingPoolDtos.get(0).getCommunityId());
        rentingPoolPo.setRentingId(rentingPoolDtos.get(0).getRentingId());

        rentingPoolPo.setState(RentingPoolDto.STATE_FINISH);

        rentingPoolInnerServiceSMOImpl.updateRentingPool(rentingPoolPo);
    }

}
