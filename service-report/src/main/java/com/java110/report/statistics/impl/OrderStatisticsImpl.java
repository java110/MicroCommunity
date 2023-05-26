package com.java110.report.statistics.impl;

import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportOrderStatisticsInnerServiceSMO;
import com.java110.report.statistics.IFeeStatistics;
import com.java110.report.statistics.IOrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 基础报表统计 实现类
 */
@Service
public class OrderStatisticsImpl implements IOrderStatistics {

    @Autowired
    private IReportOrderStatisticsInnerServiceSMO reportOrderStatisticsInnerServiceSMOImpl;


    /**
     * 查询投诉工单数
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getComplaintOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getComplaintOrderCount(queryStatisticsDto);
    }

    /**
     * 查询未处理投诉
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getUndoComplaintOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getUndoComplaintOrderCount(queryStatisticsDto);
    }

    /**
     * 查询完成投诉单
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getFinishComplaintOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getFinishComplaintOrderCount(queryStatisticsDto);
    }

    @Override
    public double getRepairOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getRepairOrderCount(queryStatisticsDto);
    }

    @Override
    public double getUndoRepairOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getUndoRepairOrderCount(queryStatisticsDto);
    }

    @Override
    public double getFinishRepairOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getFinishRepairOrderCount(queryStatisticsDto);
    }

    @Override
    public double getInspectionOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getInspectionOrderCount(queryStatisticsDto);
    }

    @Override
    public double getUndoInspectionOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getUndoInspectionOrderCount(queryStatisticsDto);
    }

    @Override
    public double getFinishInspectionOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getFinishInspectionOrderCount(queryStatisticsDto);
    }

    @Override
    public double getMaintainanceOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getMaintainanceOrderCount(queryStatisticsDto);
    }

    @Override
    public double getUndoMaintainanceOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getUndoMaintainanceOrderCount(queryStatisticsDto);
    }

    @Override
    public double getFinishMaintainanceOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getFinishMaintainanceOrderCount(queryStatisticsDto);
    }

    @Override
    public double getNotepadOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getNotepadOrderCount(queryStatisticsDto);
    }

    @Override
    public double getChargeMachineOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getChargeMachineOrderCount(queryStatisticsDto);
    }

    @Override
    public double getChargeMonthOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOrderStatisticsInnerServiceSMOImpl.getChargeMonthOrderCount(queryStatisticsDto);
    }
}
