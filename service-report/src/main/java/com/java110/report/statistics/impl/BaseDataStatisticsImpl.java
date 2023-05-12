package com.java110.report.statistics.impl;

import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IBaseDataStatistics;
import org.springframework.stereotype.Service;

/**
 * 基础数据统计类
 */
@Service
public class BaseDataStatisticsImpl implements IBaseDataStatistics {
    @Override
    public long getRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return 0;
    }

    @Override
    public long getFreeRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return 0;
    }
}
