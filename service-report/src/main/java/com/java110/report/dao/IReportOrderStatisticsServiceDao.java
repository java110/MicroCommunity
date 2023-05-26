package com.java110.report.dao;

import java.util.List;
import java.util.Map;

/**
 * 费用统计 dao 层
 */
public interface IReportOrderStatisticsServiceDao {
    /**
     * 查询投诉工单数
     *
     * @param info
     * @return
     */
    double getComplaintOrderCount(Map info);

    /**
     * 查询 投诉未完成工单
     * @param info
     * @return
     */
    double getUndoComplaintOrderCount(Map info);

    /**
     * 查询投诉完成功能
     * @param info
     * @return
     */
    double getFinishComplaintOrderCount(Map info);

    double getRepairOrderCount(Map info);

    double getUndoRepairOrderCount(Map info);

    double getFinishRepairOrderCount(Map info);

    double getInspectionOrderCount(Map info);

    double getUndoInspectionOrderCount(Map info);

    double getFinishInspectionOrderCount(Map info);
}
