package com.java110.report.statistics.impl;

import com.java110.dto.RoomDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.report.statistics.IBaseDataStatistics;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基础数据统计类
 */
@Service
public class BaseDataStatisticsImpl implements IBaseDataStatistics {

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatisticsInnerServiceSMO baseDataStatisticsInnerServiceSMOImpl;

    /**
     * 查询全部房屋
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public long getRoomCount(QueryStatisticsDto queryStatisticsDto) {

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        addRoomNumCondition(queryStatisticsDto, roomDto);
        return roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
    }


    /**
     * 查询空闲房屋
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public long getFreeRoomCount(QueryStatisticsDto queryStatisticsDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setState(RoomDto.STATE_FREE);
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        addRoomNumCondition(queryStatisticsDto, roomDto);
        return roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
    }


    /**
     * roomNum 拆分 查询房屋信息
     *
     * @param queryStatisticsDto
     * @param roomDto
     */
    private void addRoomNumCondition(QueryStatisticsDto queryStatisticsDto, RoomDto roomDto) {
        if (StringUtil.isEmpty(queryStatisticsDto.getObjName())) {
            return;
        }
        if (!queryStatisticsDto.getObjName().contains("-")) {
            roomDto.setRoomNumLike(queryStatisticsDto.getObjName());
            return;
        }
        String[] objNames = queryStatisticsDto.getObjName().split("-");
        if (objNames.length == 2) {
            roomDto.setFloorNum(objNames[0]);
            roomDto.setUnitNum("0");
            roomDto.setRoomNum(objNames[1]);
            return;
        }
        objNames = queryStatisticsDto.getObjName().split("-", 3);
        if (objNames.length == 3) {
            roomDto.setFloorNum(objNames[0]);
            roomDto.setUnitNum(objNames[1]);
            roomDto.setRoomNum(objNames[2]);
        }

    }
}
