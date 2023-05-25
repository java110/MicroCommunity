package com.java110.report.statistics;


import com.java110.dto.report.QueryStatisticsDto;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询欠费户数
     *
     * @param queryStatisticsDto
     * @return
     */
    int getOweRoomCount(QueryStatisticsDto queryStatisticsDto);

    double getCurReceivableFee(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询收费房屋数
     * @param queryStatisticsDto
     * @return
     */
    long getFeeRoomCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询楼栋费用统计信息
     * @param queryStatisticsDto
     * @return
     */
    List<Map> getFloorFeeSummary(QueryStatisticsDto queryStatisticsDto);

    /**
     * 费用项 费用统计信息
     * @param queryStatisticsDto
     * @return
     */
    List<Map> getConfigFeeSummary(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询对象明细表（数量）
     * @param queryStatisticsDto
     * @return
     */
    int getObjFeeSummaryCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询对象明细表
     * @param queryStatisticsDto
     * @return
     */
    List<Map> getObjFeeSummary(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询业主对象明细表
     * @param queryStatisticsDto
     * @return
     */
    List<Map> getOwnerFeeSummary(QueryStatisticsDto queryStatisticsDto);

    /**
     * 优惠金额
     * @param queryStatisticsDto
     * @return
     */
    double getDiscountFee(QueryStatisticsDto queryStatisticsDto);

    /**
     * 滞纳金
     * @param queryStatisticsDto
     * @return
     */
    double getLateFee(QueryStatisticsDto queryStatisticsDto);
}
