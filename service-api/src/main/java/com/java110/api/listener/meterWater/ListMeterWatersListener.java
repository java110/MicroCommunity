package com.java110.api.listener.meterWater;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeMeterWaterConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listMeterWatersListener")
public class ListMeterWatersListener extends AbstractServiceApiListener {

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeMeterWaterConstant.LIST_METERWATERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMeterWaterInnerServiceSMO getMeterWaterInnerServiceSMOImpl() {
        return meterWaterInnerServiceSMOImpl;
    }

    public void setMeterWaterInnerServiceSMOImpl(IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl) {
        this.meterWaterInnerServiceSMOImpl = meterWaterInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MeterWaterDto meterWaterDto = BeanConvertUtil.covertBean(reqJson, MeterWaterDto.class);
        ResultVo resultVo = null;
        if (!freshFeeDtoParam(meterWaterDto, reqJson)) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(1, 0, new JSONArray());
            context.setResponseEntity(responseEntity);
            return;
        }

        int count = meterWaterInnerServiceSMOImpl.queryMeterWatersCount(meterWaterDto);

        List<MeterWaterDto> meterWaterDtos = null;

        if (count > 0) {
            meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
        } else {
            meterWaterDtos = new ArrayList<>();
        }

         resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, meterWaterDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private boolean freshFeeDtoParam(MeterWaterDto meterWaterDto, JSONObject reqJson) {

        if (!reqJson.containsKey("roomNum")) {
            return true;
        }

        String roomNum = reqJson.getString("roomNum");

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
