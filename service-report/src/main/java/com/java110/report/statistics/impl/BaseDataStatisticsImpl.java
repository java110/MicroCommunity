package com.java110.report.statistics.impl;

import com.java110.dto.RoomDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.report.statistics.IBaseDataStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基础数据统计类
 */
@Service
public class BaseDataStatisticsImpl implements IBaseDataStatistics {

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Override
    public long getRoomCount(QueryStatisticsDto queryStatisticsDto) {

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        if(queryStatisticsDto.getObjName().contains("-")){
            String[] objNames = queryStatisticsDto.getObjName().split("-");
            if(objNames.length == 2){
                roomDto.setFloorNum(objNames[0]);
                roomDto.setUnitNum("0");
                roomDto.setRoomNum(objNames[1]);
            }
            objNames = queryStatisticsDto.getObjName().split("-",3);
            if(objNames.length == 3){
                roomDto.setFloorNum(objNames[0]);
                roomDto.setUnitNum(objNames[1]);
                roomDto.setRoomNum(objNames[2]);
            }
        }else{
            roomDto.setRoomNumLike(queryStatisticsDto.getObjName());
        }
        return roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
    }

    @Override
    public long getFreeRoomCount(QueryStatisticsDto queryStatisticsDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setState(RoomDto.STATE_FREE);
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        if(queryStatisticsDto.getObjName().contains("-")){
            String[] objNames = queryStatisticsDto.getObjName().split("-");
            if(objNames.length == 2){
                roomDto.setFloorNum(objNames[0]);
                roomDto.setUnitNum("0");
                roomDto.setRoomNum(objNames[1]);
            }
            objNames = queryStatisticsDto.getObjName().split("-",3);
            if(objNames.length == 3){
                roomDto.setFloorNum(objNames[0]);
                roomDto.setUnitNum(objNames[1]);
                roomDto.setRoomNum(objNames[2]);
            }
        }else{
            roomDto.setRoomNumLike(queryStatisticsDto.getObjName());
        }
        return roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
    }
}
