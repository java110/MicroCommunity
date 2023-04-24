package com.java110.fee.bmo.feeManualCollection.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.FeeManualCollectionDto;
import com.java110.dto.fee.FeeManualCollectionDetailDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.fee.bmo.feeManualCollection.ISaveFeeManualCollectionBMO;
import com.java110.intf.fee.IFeeManualCollectionDetailInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IFeeManualCollectionInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.feeManualCollection.FeeManualCollectionPo;
import com.java110.po.feeManualCollectionDetail.FeeManualCollectionDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("saveFeeManualCollectionBMOImpl")
public class SaveFeeManualCollectionBMOImpl implements ISaveFeeManualCollectionBMO {

    @Autowired
    private IFeeManualCollectionInnerServiceSMO feeManualCollectionInnerServiceSMOImpl;

    @Autowired
    private IFeeManualCollectionDetailInnerServiceSMO feeManualCollectionDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeManualCollectionPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(FeeManualCollectionPo feeManualCollectionPo) {

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(feeManualCollectionPo.getRoomId());
        roomDto.setCommunityId(feeManualCollectionPo.getCommunityId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "未找到房屋信息");

        roomDto = roomDtos.get(0);

        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjId(feeManualCollectionPo.getRoomId());
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        feeDto.setCommunityId(feeManualCollectionPo.getCommunityId());
        //查询费用信息arrearsEndTime
        feeDto.setArrearsEndTime(DateUtil.getCurrentDate());
        feeDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            feeDtos = new ArrayList<>();
            return ResultVo.createResponseEntity(feeDtos);
        }
        List<FeeDto> tmpFeeDtos = new ArrayList<>();
        for (FeeDto tmpFeeDto : feeDtos) {
            computeFeeSMOImpl.computeOweFee(tmpFeeDto);//计算欠费金额

            //如果金额为0 就排除
            if (tmpFeeDto.getFeePrice() > 0) {
                tmpFeeDtos.add(tmpFeeDto);
            }
        }

        //查询停车费
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(feeManualCollectionPo.getCommunityId());
        ownerDto.setRoomId(feeManualCollectionPo.getRoomId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);

        Assert.listOnlyOne(ownerDtos, "业主信息 存在多条或者不存在");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(feeManualCollectionPo.getCommunityId());
        ownerCarDto.setOwnerId(ownerDtos.get(0).getOwnerId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        addOwnerCarFee(ownerCarDtos, tmpFeeDtos);

        if (tmpFeeDtos.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "当前房屋不存在托收费用");
        }

        FeeManualCollectionDto feeManualCollectionDto = new FeeManualCollectionDto();
        feeManualCollectionDto.setRoomId(roomDto.getRoomId());
        feeManualCollectionDto.setCommunityId(roomDto.getCommunityId());
        List<FeeManualCollectionDto> feeManualCollectionDtos = feeManualCollectionInnerServiceSMOImpl.queryFeeManualCollections(feeManualCollectionDto);
        if (feeManualCollectionDtos == null || feeManualCollectionDtos.size() < 1) {
            feeManualCollectionPo.setOwnerId(ownerDtos.get(0).getOwnerId());
            feeManualCollectionPo.setLink(ownerDtos.get(0).getLink());
            feeManualCollectionPo.setOwnerName(ownerDtos.get(0).getName());
            feeManualCollectionPo.setState(FeeManualCollectionDto.STATE_COLLECTION);
            feeManualCollectionPo.setRoomArea(roomDto.getBuiltUpArea());
            feeManualCollectionPo.setRoomName(roomDto.getFloorNum() + "-" + roomDto.getUnitNum() + "-" + roomDto.getRoomNum());
            feeManualCollectionPo.setCollectionId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));
            int flag = feeManualCollectionInnerServiceSMOImpl.saveFeeManualCollection(feeManualCollectionPo);

            if (flag < 1) {
                return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
            }
        } else {
            feeManualCollectionPo = BeanConvertUtil.covertBean(feeManualCollectionDtos.get(0), FeeManualCollectionPo.class);
        }


        for (FeeDto tmpFeeDto : tmpFeeDtos) {
            saveFeeManualCollectionDetailInfo(tmpFeeDto, feeManualCollectionPo);
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

    private void saveFeeManualCollectionDetailInfo(FeeDto tmpFeeDto, FeeManualCollectionPo feeManualCollectionPo) {

        FeeManualCollectionDetailPo feeManualCollectionDetailPo = new FeeManualCollectionDetailPo();
        feeManualCollectionDetailPo.setAmount(tmpFeeDto.getFeePrice() + "");
        feeManualCollectionDetailPo.setCollectionId(feeManualCollectionPo.getCollectionId());
        feeManualCollectionDetailPo.setCommunityId(feeManualCollectionPo.getCommunityId());
        feeManualCollectionDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        feeManualCollectionDetailPo.setFeeId(tmpFeeDto.getFeeId());
        feeManualCollectionDetailPo.setEndTime(DateUtil.getFormatTimeString(tmpFeeDto.getDeadlineTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeManualCollectionDetailPo.setFeeName(StringUtil.isEmpty(tmpFeeDto.getImportFeeName()) ? tmpFeeDto.getFeeName() : tmpFeeDto.getImportFeeName());
        feeManualCollectionDetailPo.setStartTime(DateUtil.getFormatTimeString(tmpFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeManualCollectionDetailPo.setState(FeeManualCollectionDetailDto.STATE_COLLECTION);
        FeeManualCollectionDetailDto feeManualCollectionDetailDto = new FeeManualCollectionDetailDto();
        feeManualCollectionDetailDto.setFeeId(tmpFeeDto.getFeeId());
        feeManualCollectionDetailDto.setCollectionId(feeManualCollectionPo.getCollectionId());
        feeManualCollectionDetailDto.setCommunityId(feeManualCollectionPo.getCommunityId());
        List<FeeManualCollectionDetailDto> feeManualCollectionDetailDtos = feeManualCollectionDetailInnerServiceSMOImpl.queryFeeManualCollectionDetails(feeManualCollectionDetailDto);
        if (feeManualCollectionDetailDtos == null || feeManualCollectionDetailDtos.size() < 1) {
            feeManualCollectionDetailInnerServiceSMOImpl.saveFeeManualCollectionDetail(feeManualCollectionDetailPo);
        } else {
            feeManualCollectionDetailPo.setDetailId(feeManualCollectionDetailDtos.get(0).getDetailId());
            feeManualCollectionDetailInnerServiceSMOImpl.updateFeeManualCollectionDetail(feeManualCollectionDetailPo);
        }
    }

    /**
     * 添加车位费
     *
     * @param ownerCarDtos
     * @param tmpFeeDtos
     */
    private void addOwnerCarFee(List<OwnerCarDto> ownerCarDtos, List<FeeDto> tmpFeeDtos) {

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            return;
        }
        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            FeeDto feeDto = new FeeDto();
            feeDto.setPayerObjId(ownerCarDto.getCarId());
            feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
            feeDto.setCommunityId(ownerCarDto.getCommunityId());
            //查询费用信息arrearsEndTime
            feeDto.setArrearsEndTime(DateUtil.getCurrentDate());
            feeDto.setState(FeeDto.STATE_DOING);
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

            if (feeDtos == null || feeDtos.size() < 1) {
                return;
            }
            for (FeeDto tmpFeeDto : feeDtos) {
                computeFeeSMOImpl.computeOweFee(tmpFeeDto);//计算欠费金额

                //如果金额为0 就排除
                if (tmpFeeDto.getFeePrice() > 0) {
                    tmpFeeDtos.add(tmpFeeDto);
                }
            }

        }
    }

}
