package com.java110.job.adapt.fee.asyn.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.entity.order.Business;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.adapt.fee.asyn.IUpdateFeeOwnerInfo;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateFeeOwnerInfoImpl implements IUpdateFeeOwnerInfo {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;


    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;
    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;


    @Override
    @Async
    public void doUpdate(Business business, JSONObject businessOwner) {
        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerPo.getMemberId());
        ownerDto.setCommunityId(ownerPo.getCommunityId());
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        if (ownerDtos == null || ownerDtos.size() <1) { // 不是业主 不管他
            return;
        }

        List<FeeDto> feeDtos = getRoomFee(ownerPo);
        if (feeDtos != null) {
            for (FeeDto tmpFeeDto : feeDtos) {
                doDealFeeDto(tmpFeeDto, ownerDtos.get(0));
            }
        }

        feeDtos = getOwnerCarFee(ownerPo);
        if (feeDtos != null) {
            for (FeeDto tmpFeeDto : feeDtos) {
                doDealFeeDto(tmpFeeDto, ownerDtos.get(0));
            }
        }


    }

    private List<FeeDto> getOwnerCarFee(OwnerPo ownerPo) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setOwnerId(ownerPo.getOwnerId());
        //这种情况说明 业主已经删掉了 需要查询状态为 1 的数据
        List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        //没有房屋时返回
        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            return null;
        }


        //拿到小区ID
        String communityId = ownerPo.getCommunityId();

        List<String> payerObjIds = new ArrayList<>();
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            payerObjIds.add(tmpOwnerCarDto.getCarId());
        }
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(communityId);
        feeDto.setPayerObjIds(payerObjIds.toArray(new String[payerObjIds.size()]));
        feeDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        return feeDtos;
    }

    private void doDealFeeDto(FeeDto tmpFeeDto, OwnerDto ownerDto) {

        //判断是否存在 业主名称

        FeeAttrDto ownerNameAttr = getOwnerFeeAttr(tmpFeeDto, FeeAttrDto.SPEC_CD_OWNER_NAME);
        FeeAttrPo tmpFeeAttrPo = new FeeAttrPo();
        if (ownerNameAttr == null) {
            tmpFeeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            tmpFeeAttrPo.setCommunityId(ownerDto.getCommunityId());
            tmpFeeAttrPo.setFeeId(tmpFeeDto.getFeeId());
            tmpFeeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
            tmpFeeAttrPo.setValue(ownerDto.getName());
            feeAttrInnerServiceSMOImpl.saveFeeAttr(tmpFeeAttrPo);
        } else {
            tmpFeeAttrPo.setAttrId(ownerNameAttr.getAttrId());
            tmpFeeAttrPo.setValue(ownerDto.getName());
            feeAttrInnerServiceSMOImpl.updateFeeAttr(tmpFeeAttrPo);
        }

        //判断是否存在业主联系电话
        ownerNameAttr = getOwnerFeeAttr(tmpFeeDto, FeeAttrDto.SPEC_CD_OWNER_LINK);
        tmpFeeAttrPo = new FeeAttrPo();
        if (ownerNameAttr == null) {
            tmpFeeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            tmpFeeAttrPo.setCommunityId(ownerDto.getCommunityId());
            tmpFeeAttrPo.setFeeId(tmpFeeDto.getFeeId());
            tmpFeeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
            tmpFeeAttrPo.setValue(ownerDto.getLink());
            feeAttrInnerServiceSMOImpl.saveFeeAttr(tmpFeeAttrPo);
        } else {
            tmpFeeAttrPo.setAttrId(ownerNameAttr.getAttrId());
            tmpFeeAttrPo.setValue(ownerDto.getLink());
            feeAttrInnerServiceSMOImpl.updateFeeAttr(tmpFeeAttrPo);
        }

    }

    private FeeAttrDto getOwnerFeeAttr(FeeDto tmpFeeDto, String specCd) {
        List<FeeAttrDto> feeAttrDtos = tmpFeeDto.getFeeAttrDtos();
        if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
            return null;
        }

        for (FeeAttrDto feeAttrDto : feeAttrDtos) {
            if (!specCd.equals(feeAttrDto.getSpecCd())) {
                continue;
            }
            return feeAttrDto;
        }
        return null;
    }


    public List<FeeDto> getRoomFee(OwnerPo ownerPo) {
        RoomDto roomDto = new RoomDto();
        roomDto.setOwnerId(ownerPo.getOwnerId());
        //这种情况说明 业主已经删掉了 需要查询状态为 1 的数据
        List<RoomDto> rooms = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);

        //没有房屋时返回
        if (rooms == null || rooms.size() < 1) {
            return null;
        }


        //拿到小区ID
        String communityId = ownerPo.getCommunityId();

        List<String> payerObjIds = new ArrayList<>();
        for (RoomDto tRoomDto : rooms) {
            payerObjIds.add(tRoomDto.getUnitId());
            payerObjIds.add(tRoomDto.getRoomId());
            payerObjIds.add(tRoomDto.getFloorId());
        }
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(communityId);
        feeDto.setPayerObjIds(payerObjIds.toArray(new String[payerObjIds.size()]));
        feeDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        return feeDtos;
    }

}
