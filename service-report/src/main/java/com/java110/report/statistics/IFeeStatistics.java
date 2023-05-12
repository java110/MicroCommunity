package com.java110.report.statistics;


import com.java110.dto.report.QueryStatisticsDto;

/**
 * 费用统计类
 */
public interface IFeeStatistics {

    /**
     * 查询历史 欠费功能
     *
     * @return
     */
    double getHisMonthOweFee(QueryStatisticsDto queryFeeStatisticsDto);

    /**
     * 查询 当月欠费
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    double getCurMonthOweFee(QueryStatisticsDto queryFeeStatisticsDto);


    /**
     * 欠费追回
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    double getHisReceivedFee(QueryStatisticsDto queryFeeStatisticsDto);

    /**
     * 预交费用
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    double getPreReceivedFee(QueryStatisticsDto queryFeeStatisticsDto);

    /**
     * 查询实收数据
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    double getReceivedFee(QueryStatisticsDto queryFeeStatisticsDto);

}
