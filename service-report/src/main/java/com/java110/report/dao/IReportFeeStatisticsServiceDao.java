package com.java110.report.dao;

import java.util.List;
import java.util.Map;

/**
 * 费用统计 dao 层
 */
public interface IReportFeeStatisticsServiceDao {
    /**
     * 历史欠费
     *
     * @param info
     * @return
     */
    double getHisMonthOweFee(Map info);

    /**
     * 查询当月欠费
     *
     * @param info
     * @return
     */
    double getCurMonthOweFee(Map info);

    /**
     * 查询欠费追回
     *
     * @param info
     * @return
     */
    double getHisReceivedFee(Map info);

    /**
     * 查询 预交费用
     *
     * @param info
     * @return
     */
    double getPreReceivedFee(Map info);

    /**
     * 查询 实收费用
     *
     * @param info
     * @return
     */
    double getReceivedFee(Map info);

    /**
     * 查询欠费户数
     *
     * @param info
     * @return
     */
    int getOweRoomCount(Map info);

    /**
     * 查询当月应收费用
     *
     * @param info
     * @return
     */
    double getCurReceivableFee(Map info);

    /**
     * 查询收费房屋数
     *
     * @param info
     * @return
     */
    int getFeeRoomCount(Map info);

    /**
     * 统计楼栋 收费率信息
     *
     * @param info
     * @return
     */
    List<Map> getFloorFeeSummary(Map info);

    /**
     * 统计费用项 收费率信息
     *
     * @param info
     * @return
     */
    List<Map> getConfigFeeSummary(Map info);

    /**
     * 查询费用明细表（总数）
     *
     * @param info
     * @return
     */
    int getObjFeeSummaryCount(Map info);

    /**
     * 查询费用明细表
     *
     * @param info
     * @return
     */
    List<Map> getObjFeeSummary(Map info);

    /**
     * 查询业主明细表
     *
     * @param info
     * @return
     */
    List<Map> getOwnerFeeSummary(Map info);

    /**
     * 查询优惠费用
     * @param info
     * @return
     */
    double getDiscountFee(Map info);

    /**
     * 查询滞纳金
     * @param info
     * @return
     */
    double getLateFee(Map info);
}
