package com.java110.store.bmo.contract.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.RoomDto;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.common.IContractApplyUserInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.contract.ContractPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.room.RoomPo;
import com.java110.store.bmo.contract.IDeleteContractBMO;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("deleteContractBMOImpl")
public class DeleteContractBMOImpl implements IDeleteContractBMO {

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IContractApplyUserInnerServiceSMO contractApplyUserInnerServiceSMOImpl;

    /**
     * @param contractPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ContractPo contractPo) {

        int flag = contractInnerServiceSMOImpl.deleteContract(contractPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        deleteContractRoomRel(contractPo);
//删除流程信息
        contractApplyUserInnerServiceSMOImpl.deleteTask(contractPo);
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }


    private void deleteContractRoomRel(ContractPo contractPo) {

        // 查询合同关联房屋
        ContractRoomDto contractRoomDto = new ContractRoomDto();
        contractRoomDto.setContractId(contractPo.getContractId());
        contractRoomDto.setStoreId(contractPo.getStoreId());
        List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);

        if (contractRoomDtos == null || contractRoomDtos.size() < 1) {
            return;
        }
        List<ContractRoomDto> tmpContractRoomDtos = null;
        OwnerRoomRelPo ownerRoomRelPo = null;
        for (ContractRoomDto tmpContractRoomDto : contractRoomDtos) {

            //判断房屋是否还签订了其他的合同
            contractRoomDto = new ContractRoomDto();
            contractRoomDto.setRoomId(tmpContractRoomDto.getRoomId());
            contractRoomDto.setStoreId(contractPo.getStoreId());
            tmpContractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);

            //还有其他的合同 先不处理
            if (tmpContractRoomDtos != null && tmpContractRoomDtos.size() > 1) {
                continue;
            }


            //刷业主
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(tmpContractRoomDto.getRoomId());
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

            if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) { // 说明房屋没有业主
                continue;
            }

            validateRoom(tmpContractRoomDto);

            //修改房屋状态
            RoomPo roomPo = new RoomPo();
            roomPo.setRoomId(ownerRoomRelDtos.get(0).getRoomId());
            roomPo.setState(RoomDto.STATE_FREE);
            roomPo.setStatusCd(StatusConstant.STATUS_CD_VALID);
            roomInnerServiceSMOImpl.updateRooms(roomPo);

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
        }

    }

    /**
     * 房屋是否欠费校验
     *
     * @param tmpContractRoomDto
     */
    private void validateRoom(ContractRoomDto tmpContractRoomDto) {
        //校验 房屋上是否有费用存在


        //判断房屋是否存在
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(tmpContractRoomDto.getRoomId());
        roomDto.setCommunityId(tmpContractRoomDto.getCommunityId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋不存在");

        //查询房屋时候有欠费
        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        feeDto.setPayerObjId(tmpContractRoomDto.getRoomId());
        feeDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (feeDtos != null && feeDtos.size() > 0) {
            throw new IllegalArgumentException(roomDtos.get(0).getRoomNum() + "房屋存在未结束的费用 请先处理");
        }

    }

}
