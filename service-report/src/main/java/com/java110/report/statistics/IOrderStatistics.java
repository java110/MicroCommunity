package com.java110.report.statistics;


import com.java110.dto.report.QueryStatisticsDto;

import java.util.List;
import java.util.Map;

/**
 * 工单类统计类
 */
public interface IOrderStatistics {


    /**
     * 查询投诉工单数
     * @param queryStatisticsDto
     * @return
     */
    double getComplaintOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询未处理投诉单
     * @param queryStatisticsDto
     * @return
     */
    double getUndoComplaintOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询处理完成投诉
     * @param queryStatisticsDto
     * @return
     */
    double getFinishComplaintOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询报修单
     * @param queryStatisticsDto
     * @return
     */
    double getRepairOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询未完成报修单
     * @param queryStatisticsDto
     * @return
     */
    double getUndoRepairOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询已完成报修单
     * @param queryStatisticsDto
     * @return
     */
    double getFinishRepairOrderCount(QueryStatisticsDto queryStatisticsDto);

    double getInspectionOrderCount(QueryStatisticsDto queryStatisticsDto);

    double getUndoInspectionOrderCount(QueryStatisticsDto queryStatisticsDto);

    double getFinishInspectionOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询保养单
     * @param queryStatisticsDto
     * @return
     */
    double getMaintainanceOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询未完成保养
     * @param queryStatisticsDto
     * @return
     */
    double getUndoMaintainanceOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询已完成保养
     * @param queryStatisticsDto
     * @return
     */
    double getFinishMaintainanceOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询业主反馈
     * @param queryStatisticsDto
     * @return
     */
    double getNotepadOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询充电金额
     * @param queryStatisticsDto
     * @return
     */
    double getChargeMachineOrderCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询月卡实收
     * @param queryStatisticsDto
     * @return
     */
    double getChargeMonthOrderCount(QueryStatisticsDto queryStatisticsDto);
}
