package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.meter.ImportExportMeterWaterDto;
import com.java110.dto.meter.MeterWaterDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeDetailRefreshFeeMonthDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.meter.MeterWaterPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("importMeterWaterFeeQueueData")
public class ImportMeterWaterFeeQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonthInnerServiceSMO payFeeMonthInnerServiceSMOImpl;

    //    @Autowired
//    private IPayFeeMonth payFeeMonthImpl;
    @Override
    public void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {

        for (AssetImportLogDetailDto assetImportLogDetailDto : assetImportLogDetailDtos) {

            try {
                doImportData(assetImportLogDetailDto);
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId());
            } catch (Exception e) {
                e.printStackTrace();
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId(), e);
            }
        }
    }

    private void doImportData(AssetImportLogDetailDto assetImportLogDetailDto) {
        JSONObject data = JSONObject.parseObject(assetImportLogDetailDto.getContent());
        ImportExportMeterWaterDto importExportMeterWaterDto = BeanConvertUtil.covertBean(data, ImportExportMeterWaterDto.class);

        String communityId = importExportMeterWaterDto.getCommunityId();
        String storeId = importExportMeterWaterDto.getStoreId();
        String configId = importExportMeterWaterDto.getConfigId();
        String userId = importExportMeterWaterDto.getUserId();
        String feeTypeCd = importExportMeterWaterDto.getFeeTypeCd();
        String batchId = importExportMeterWaterDto.getBatchId();
        String meterType = importExportMeterWaterDto.getMeterType();
        JSONObject meteWaterJson = null;

        List<PayFeePo> fees = new ArrayList<>();
        List<MeterWaterPo> meterWaterPos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();

        dealImportExportMeterWater(importExportMeterWaterDto,
                communityId,
                storeId,
                configId,
                userId,
                feeTypeCd,
                fees,
                meterWaterPos,
                feeAttrPos,
                batchId,
                meterType
        );


        if (fees.size() < 1 || meterWaterPos.size() < 1) {
            throw new IllegalArgumentException("批量抄表失败");
        }

        feeInnerServiceSMOImpl.saveFee(fees);

        if (feeAttrPos.size() > 0) {
            feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);
        }


        meterWaterInnerServiceSMOImpl.saveMeterWaters(meterWaterPos);

        // todo 这里异步的方式计算 月数据 和欠费数据

        PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto = new PayFeeDetailRefreshFeeMonthDto();
        payFeeDetailRefreshFeeMonthDto.setCommunityId(communityId);
        payFeeDetailRefreshFeeMonthDto.setFeeId(fees.get(0).getFeeId());
        payFeeMonthInnerServiceSMOImpl.doGeneratorOrRefreshFeeMonth(payFeeDetailRefreshFeeMonthDto);
    }

    private void dealImportExportMeterWater(ImportExportMeterWaterDto importExportMeterWaterDto, String communityId,
                                            String storeId, String configId, String userId, String feeTypeCd,
                                            List<PayFeePo> fees, List<MeterWaterPo> meterWaterPos, List<FeeAttrPo> feeAttrPos,
                                            String batchId, String meterType) {

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId);
        roomDto.setFloorNum(importExportMeterWaterDto.getFloorNum());
        roomDto.setUnitNum(importExportMeterWaterDto.getUnitNum());
        roomDto.setRoomNum(importExportMeterWaterDto.getRoomNum());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋未找到或找到多条" + importExportMeterWaterDto.getFloorNum() + "-" + importExportMeterWaterDto.getUnitNum() + "-" + importExportMeterWaterDto.getRoomNum());

        String roomName = importExportMeterWaterDto.getFloorNum() + "-" + importExportMeterWaterDto.getUnitNum() + "-" + importExportMeterWaterDto.getRoomNum();

        importExportMeterWaterDto.setMeterType(meterType);
        //查询房屋是否有合同
        ContractRoomDto contractRoomDto = new ContractRoomDto();

        contractRoomDto.setRoomId(roomDtos.get(0).getRoomId());

        List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);


        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId, true));
        payFeePo.setIncomeObjId(storeId);
        payFeePo.setAmount("-1");
        payFeePo.setStartTime(importExportMeterWaterDto.getPreReadingTime());
        payFeePo.setEndTime(importExportMeterWaterDto.getPreReadingTime());
        payFeePo.setPayerObjId(roomDtos.get(0).getRoomId());
        payFeePo.setBatchId(batchId);
        //payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);

        if (contractRoomDtos != null && contractRoomDtos.size() > 0) {
            payFeePo.setPayerObjId(contractRoomDtos.get(0).getContractId());
            //payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CONTRACT);
            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
            String feeName = roomName;
            if ("1010".equals(importExportMeterWaterDto.getMeterType())) {
                feeName += "水费";
            } else if ("2020".equals(importExportMeterWaterDto.getMeterType())) {
                feeName += "电费";
            } else {
                feeName += "燃气费";
            }
            feeAttrPo.setValue(feeName);
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);
        }


        //todo 保存房屋名称
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(communityId);
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME);
        feeAttrPo.setValue(roomName);
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
        feeAttrPos.add(feeAttrPo);

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setRoomId(roomDtos.get(0).getRoomId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);

        if (ownerDtos != null && ownerDtos.size() > 0) {
            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
            feeAttrPo.setValue(ownerDtos.get(0).getOwnerId());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPos.add(feeAttrPo);

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
            feeAttrPo.setValue(ownerDtos.get(0).getLink());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPos.add(feeAttrPo);

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
            feeAttrPo.setValue(ownerDtos.get(0).getName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPos.add(feeAttrPo);

        }
        payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setState(FeeDto.STATE_DOING);
        //todo 如果 当前读数小于等于上期读数
        //todo 这里注释，本来想着 方便物业再不用去缴费 读数为0 的，结果物业还习惯不了 总以为读数为0 的没有抄表成功，不会去结束费用中查看
//        if (Double.parseDouble(importExportMeterWaterDto.getCurDegrees()) <= Double.parseDouble(importExportMeterWaterDto.getPreDegrees())) {
//            payFeePo.setState(FeeDto.STATE_FINISH);
//        }

        payFeePo.setUserId(userId);
        payFeePo.setFeeTypeCd(feeTypeCd);
        payFeePo.setConfigId(configId);
        payFeePo.setCommunityId(communityId);
        fees.add(payFeePo);


        MeterWaterPo meterWaterPo = new MeterWaterPo();
        meterWaterPo.setCommunityId(communityId);
        meterWaterPo.setCurDegrees(importExportMeterWaterDto.getCurDegrees());
        meterWaterPo.setCurReadingTime(importExportMeterWaterDto.getCurReadingTime());
        meterWaterPo.setFeeId(payFeePo.getFeeId());
        meterWaterPo.setMeterType(importExportMeterWaterDto.getMeterType());
        meterWaterPo.setObjId(roomDtos.get(0).getRoomId());
        meterWaterPo.setObjName(importExportMeterWaterDto.getFloorNum() + "-" + importExportMeterWaterDto.getUnitNum() + "-" + importExportMeterWaterDto.getRoomNum());
        meterWaterPo.setObjType(MeterWaterDto.OBJ_TYPE_ROOM);
        meterWaterPo.setPreDegrees(importExportMeterWaterDto.getPreDegrees());
        meterWaterPo.setPreReadingTime(importExportMeterWaterDto.getPreReadingTime());
        meterWaterPo.setWaterId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_waterId, true));
        meterWaterPo.setRemark(importExportMeterWaterDto.getRemark());
        meterWaterPo.setPrice(importExportMeterWaterDto.getPrice());
        meterWaterPos.add(meterWaterPo);
    }
}
