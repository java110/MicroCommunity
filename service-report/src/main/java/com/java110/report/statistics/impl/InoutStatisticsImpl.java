package com.java110.report.statistics.impl;

import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportInoutStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportOrderStatisticsInnerServiceSMO;
import com.java110.report.statistics.IInoutStatistics;
import com.java110.report.statistics.IOrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基础报表统计 实现类
 */
@Service
public class InoutStatisticsImpl implements IInoutStatistics {

    @Autowired
    private IReportInoutStatisticsInnerServiceSMO reportInoutStatisticsInnerServiceSMOImpl;


    /**
     * 查询投诉工单数
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public long getCarInCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.getCarInCount(queryStatisticsDto);
    }

    @Override
    public long getCarOutCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.getCarOutCount(queryStatisticsDto);
    }

    @Override
    public long getPersonInCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.getPersonInCount(queryStatisticsDto);
    }

    @Override
    public long getPersonFaceToMachineCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.getPersonFaceToMachineCount(queryStatisticsDto);
    }

    @Override
    public long purchaseInCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.purchaseInCount(queryStatisticsDto);

    }

    @Override
    public long purchaseOutCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.purchaseOutCount(queryStatisticsDto);
    }

    @Override
    public double purchaseInAmount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.purchaseInAmount(queryStatisticsDto);
    }

    @Override
    public double purchaseOutAmount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.purchaseOutAmount(queryStatisticsDto);
    }

    @Override
    public long allocationCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.allocationCount(queryStatisticsDto);
    }

    @Override
    public long roomRenovationCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.roomRenovationCount(queryStatisticsDto);
    }

    @Override
    public long itemReleaseCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.itemReleaseCount(queryStatisticsDto);
    }

    @Override
    public long roomInCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.roomInCount(queryStatisticsDto);
    }

    @Override
    public long roomOutCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.roomOutCount(queryStatisticsDto);
    }

    @Override
    public long ownerRegisterCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.ownerRegisterCount(queryStatisticsDto);
    }

    @Override
    public long noAttendanceCount(QueryStatisticsDto queryStatisticsDto) {
        return reportInoutStatisticsInnerServiceSMOImpl.noAttendanceCount(queryStatisticsDto);
    }

}
