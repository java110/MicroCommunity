package com.java110.community.bmo.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.assets.IQueryAssetsBMO;
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
public class QueryAssetsBMOImpl implements IQueryAssetsBMO {

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

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
        JSONObject data = new JSONObject();
        int floorCount = floorInnerServiceSMOImpl.queryFloorsCount(communityId);

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId);
        //1010301表示房屋  2020602表示商铺
        roomDto.setRoomType("1010301");
        int roomCount = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(communityId);
        int parkingSpaceCount = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);

        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityId);
        int machineCount = machineInnerServiceSMOImpl.queryMachinesCount(machineDto);
        data.put("floorCount", floorCount);
        data.put("roomCount", roomCount);
        data.put("parkingSpaceCount", parkingSpaceCount);
        data.put("machineCount", machineCount);
        return ResultVo.createResponseEntity(data);
    }
}
