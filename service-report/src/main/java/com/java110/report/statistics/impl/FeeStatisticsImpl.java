package com.java110.report.statistics.impl;

import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IFeeStatistics;
import org.springframework.stereotype.Service;

/**
 * 基础报表统计 实现类
 */
@Service
public class FeeStatisticsImpl implements IFeeStatistics {

    /**
     * 查询 历史欠费
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getHisMonthOweFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return 0;
    }

    @Override
    public double getCurMonthOweFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return 0;
    }

    @Override
    public double getHisReceivedFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return 0;
    }

    @Override
    public double getPreReceivedFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return 0;
    }

    @Override
    public double getReceivedFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return 0;
    }
}
