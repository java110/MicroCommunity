package com.java110.community.bmo.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.assets.IQueryAssetsBMO;
import com.java110.community.bmo.assets.IQueryAssetsRoomBMO;
import com.java110.dto.RoomDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QueryAssetsRoomBMOImpl implements IQueryAssetsRoomBMO {


    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    /**
     * @param communityId
     * @return {
     * data:{
     * floorCount:30,
     * roomCount:29,
     * parkingSpaceCount:12,
     * machineCount:12
     * }
     * }
     */
    @Override
    public ResponseEntity<String> query(String communityId) {

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId);
        roomDto.setState(RoomDto.STATE_SELL);
        int sellRoomCount = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);

        roomDto.setState(RoomDto.STATE_FREE);
        int freeRoomCount = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);
        JSONObject data = new JSONObject();
        data.put("sellRoomCount", sellRoomCount);
        data.put("freeRoomCount", freeRoomCount);
        return ResultVo.createResponseEntity(data);
    }
}
