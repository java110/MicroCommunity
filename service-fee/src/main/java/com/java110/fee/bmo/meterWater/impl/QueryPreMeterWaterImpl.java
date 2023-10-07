package com.java110.fee.bmo.meterWater.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.room.RoomDto;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.meter.ImportExportMeterWaterDto;
import com.java110.dto.meter.MeterWaterDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.fee.bmo.meterWater.IQueryPreMeterWater;
import com.java110.fee.feeMonth.IPayFeeMonth;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.meter.MeterWaterPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 上期度数查询
 */
@Service
public class QueryPreMeterWaterImpl implements IQueryPreMeterWater {

    private static Logger logger = LoggerFactory.getLogger(QueryPreMeterWaterImpl.class);

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
    private IPayFeeMonth payFeeMonthImpl;

    @Override
    public ResponseEntity<String> query(MeterWaterDto meterWaterDto, String roomNum) {

        if (!freshFeeDtoParam(meterWaterDto, roomNum)) {
            return ResultVo.createResponseEntity(1, 0, new JSONArray());
        }

        List<MeterWaterDto> meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
        int total = meterWaterDtos == null ? 0 : meterWaterDtos.size();
        return ResultVo.createResponseEntity(1, total, meterWaterDtos);
    }

    @Override
    public ResponseEntity<String> queryExportRoomAndMeterWater(String communityId, String meterType) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId);
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        MeterWaterDto meterWaterDto = null;
        List<ImportExportMeterWaterDto> importExportMeterWaterDtos = new ArrayList<>();
        ImportExportMeterWaterDto importExportMeterWaterDto = null;

        for (RoomDto tmpRoomDto : roomDtos) {
            meterWaterDto = new MeterWaterDto();
            meterWaterDto.setMeterType(meterType);
            meterWaterDto.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            meterWaterDto.setObjId(tmpRoomDto.getRoomId());
            meterWaterDto.setCommunityId(communityId);
            List<MeterWaterDto> meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
            importExportMeterWaterDto = BeanConvertUtil.covertBean(tmpRoomDto, ImportExportMeterWaterDto.class);
            String preDegree = "0";
            String preReadTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B);
            double price = 0;
            if (meterWaterDtos != null && meterWaterDtos.size() > 0) {
                preDegree = meterWaterDtos.get(0).getCurDegrees();
                preReadTime = DateUtil.dateTimeToDate(meterWaterDtos.get(0).getCurReadingTime());
                price = meterWaterDtos.get(0).getPrice();
            }
            importExportMeterWaterDto.setPreDegrees(preDegree);
            importExportMeterWaterDto.setPreReadingTime(preReadTime);
            importExportMeterWaterDto.setPrice(price);
            importExportMeterWaterDtos.add(importExportMeterWaterDto);
        }
        return ResultVo.createResponseEntity(1, importExportMeterWaterDtos.size(), importExportMeterWaterDtos);
    }

    @Override
    public ResponseEntity<String> importMeterWater(JSONObject reqJson) {
        String communityId = reqJson.getString("communityId");
        String storeId = reqJson.getString("storeId");
        String configId = reqJson.getString("configId");
        String userId = reqJson.getString("userId");
        String feeTypeCd = reqJson.getString("feeTypeCd");
        String batchId = reqJson.getString("batchId");
        String meterType = reqJson.getString("meterType");
        JSONArray importMeteWaterFees = reqJson.getJSONArray("importMeteWaterFees");
        JSONObject meteWaterJson = null;
        ImportExportMeterWaterDto importExportMeterWaterDto = null;

        List<PayFeePo> fees = new ArrayList<>();
        List<MeterWaterPo> meterWaterPos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        for (int meteWaterIndex = 0; meteWaterIndex < importMeteWaterFees.size(); meteWaterIndex++) {
            meteWaterJson = importMeteWaterFees.getJSONObject(meteWaterIndex);

            importExportMeterWaterDto = BeanConvertUtil.covertBean(meteWaterJson, ImportExportMeterWaterDto.class);

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
        }

        if (fees.size() < 1 || meterWaterPos.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "批量抄表失败");
        }

        feeInnerServiceSMOImpl.saveFee(fees);

        if (feeAttrPos.size() > 0) {
            feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);
        }


        meterWaterInnerServiceSMOImpl.saveMeterWaters(meterWaterPos);

        // todo 这里异步的方式计算 月数据 和欠费数据
        List<String> feeIds = new ArrayList<>();
        for (PayFeePo feePo : fees) {
            feeIds.add(feePo.getFeeId());
        }
        payFeeMonthImpl.doGeneratorFeeMonths(feeIds, fees.get(0).getCommunityId());
        return ResultVo.success();
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
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId,true));
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
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId,true));
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
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId,true));
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
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId,true));
            feeAttrPos.add(feeAttrPo);

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
            feeAttrPo.setValue(ownerDtos.get(0).getLink());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId,true));
            feeAttrPos.add(feeAttrPo);

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
            feeAttrPo.setValue(ownerDtos.get(0).getName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId,true));
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
        meterWaterPo.setWaterId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_waterId,true));
        meterWaterPo.setRemark(importExportMeterWaterDto.getRemark());
        meterWaterPo.setPrice(importExportMeterWaterDto.getPrice());
        meterWaterPos.add(meterWaterPo);
    }

    private boolean freshFeeDtoParam(MeterWaterDto meterWaterDto, String roomNum) {

        if (StringUtil.isEmpty(roomNum)) {
            return true;
        }

        if (!roomNum.contains("-")) {
            return false;
        }
        if (MeterWaterDto.METER_TYPE_ROOM.equals(meterWaterDto.getObjType())) {
            String[] nums = roomNum.split("-", 3);
            if (nums.length != 3) {
                return false;
            }
            RoomDto roomDto = new RoomDto();
            roomDto.setFloorNum(nums[0]);
            roomDto.setUnitNum(nums[1]);
            roomDto.setRoomNum(nums[2]);
            roomDto.setCommunityId(meterWaterDto.getCommunityId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

            if (roomDtos == null || roomDtos.size() < 1) {
                return false;
            }
            meterWaterDto.setObjId(roomDtos.get(0).getRoomId());

        } else {
            String[] nums = roomNum.split("-", 2);
            if (nums.length != 2) {
                return false;
            }
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setAreaNum(nums[0]);
            parkingSpaceDto.setNum(nums[1]);
            parkingSpaceDto.setCommunityId(meterWaterDto.getCommunityId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
                return false;
            }
            meterWaterDto.setObjId(parkingSpaceDtos.get(0).getPsId());
        }

        return true;
    }
}
