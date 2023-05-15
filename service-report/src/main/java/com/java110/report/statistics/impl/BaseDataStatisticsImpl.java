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
        return roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
    }

    @Override
    public long getFreeRoomCount(QueryStatisticsDto queryStatisticsDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setState(RoomDto.STATE_FREE);
        return roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
    }
}
