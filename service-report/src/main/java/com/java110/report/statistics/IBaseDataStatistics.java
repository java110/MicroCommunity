package com.java110.report.statistics;

import com.java110.dto.RoomDto;
import com.java110.dto.report.QueryStatisticsDto;

import java.util.List;

/**
 * 基础数据统计
 */
public interface IBaseDataStatistics {

    /**
     * 查询房屋数
     * @param queryStatisticsDto
     * @return
     */
    long getRoomCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询房屋
     * @param queryStatisticsDto
     * @return
     */
    List<RoomDto> getRoomInfo(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询空闲房屋数
     * @param queryStatisticsDto
     * @return
     */
    long getFreeRoomCount(QueryStatisticsDto queryStatisticsDto);





}
