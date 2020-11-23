package com.java110.fee.bmo.meterWater.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.meterWater.ImportExportMeterWaterDto;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.fee.bmo.meterWater.IQueryPreMeterWater;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.po.meterWater.MeterWaterPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            List<MeterWaterDto> meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
            importExportMeterWaterDto = BeanConvertUtil.covertBean(tmpRoomDto, ImportExportMeterWaterDto.class);
            String preDegree = meterWaterDtos == null ? "0" : meterWaterDtos.get(0).getCurDegrees();
            String preReadTime = meterWaterDtos == null ? DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A)
                    : meterWaterDtos.get(0).getCurReadingTime();

            importExportMeterWaterDto.setPreDegrees(preDegree);
            importExportMeterWaterDto.setPreReadingTime(preReadTime);
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
        JSONArray importMeteWaterFees = reqJson.getJSONArray("importMeteWaterFees");
        JSONObject meteWaterJson = null;
        ImportExportMeterWaterDto importExportMeterWaterDto = null;

        List<PayFeePo> fees = new ArrayList<>();
        List<MeterWaterPo> meterWaterPos = new ArrayList<>();
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
                    meterWaterPos
            );
        }

        if (fees.size() < 1 || meterWaterPos.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "批量抄表失败");
        }

        feeInnerServiceSMOImpl.saveFee(fees);

        meterWaterInnerServiceSMOImpl.saveMeterWaters(meterWaterPos);
        return ResultVo.success();
    }

    private void dealImportExportMeterWater(ImportExportMeterWaterDto importExportMeterWaterDto, String communityId,
                                            String storeId, String configId, String userId, String feeTypeCd,
                                            List<PayFeePo> fees, List<MeterWaterPo> meterWaterPos) {

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId);
        roomDto.setFloorNum(importExportMeterWaterDto.getFloorNum());
        roomDto.setUnitNum(importExportMeterWaterDto.getUnitNum());
        roomDto.setRoomNum(importExportMeterWaterDto.getRoomNum());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋未找到或找到多条" + importExportMeterWaterDto.getFloorNum() + "-" + importExportMeterWaterDto.getUnitNum() + "-" + importExportMeterWaterDto.getRoomNum());

        if (FeeConfigDto.FEE_TYPE_CD_WATER.equals(feeTypeCd)) {
            importExportMeterWaterDto.setMeterType("1010");
        } else {
            importExportMeterWaterDto.setMeterType("2020");
        }

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setIncomeObjId(storeId);
        payFeePo.setAmount("-1");
        payFeePo.setStartTime(importExportMeterWaterDto.getPreReadingTime());
        payFeePo.setEndTime(importExportMeterWaterDto.getPreReadingTime());
        payFeePo.setPayerObjId(roomDtos.get(0).getRoomId());
        //payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setState(FeeDto.STATE_DOING);
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
        meterWaterPo.setObjName(importExportMeterWaterDto.getFloorNum() + "栋" + importExportMeterWaterDto.getUnitNum() + "单元" + importExportMeterWaterDto.getRoomNum() + "室");
        meterWaterPo.setObjType(MeterWaterDto.OBJ_TYPE_ROOM);
        meterWaterPo.setPreDegrees(importExportMeterWaterDto.getPreDegrees());
        meterWaterPo.setPreReadingTime(importExportMeterWaterDto.getPreReadingTime());
        meterWaterPo.setWaterId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_waterId));
        meterWaterPo.setRemark(importExportMeterWaterDto.getRemark());
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
            String[] nums = roomNum.split("-");
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
            String[] nums = roomNum.split("-");
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
