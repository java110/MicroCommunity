package com.java110.fee.bmo.meterWater.impl;

import com.alibaba.fastjson.JSONArray;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.meterWater.ImportExportMeterWaterDto;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.fee.bmo.meterWater.IQueryPreMeterWater;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
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
