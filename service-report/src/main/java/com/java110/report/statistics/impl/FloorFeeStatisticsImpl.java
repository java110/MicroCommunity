package com.java110.report.statistics.impl;

import com.java110.dto.report.QueryStatisticsDto;
import com.java110.dto.report.ReportFloorFeeStatisticsDto;
import com.java110.intf.report.IReportFloorFeeStatisticsInnerServiceSMO;
import com.java110.report.statistics.IFloorFeeStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FloorFeeStatisticsImpl implements IFloorFeeStatistics {
    @Autowired
    private IReportFloorFeeStatisticsInnerServiceSMO reportFloorFeeStatisticsInnerServiceSMOImpl;
    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorOweRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorOweRoomCount(queryStatisticsDto);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorFeeRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorFeeRoomCount(queryStatisticsDto);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorReceivedFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorReceivedFee(queryStatisticsDto);

    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorPreReceivedFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorPreReceivedFee(queryStatisticsDto);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorHisOweFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorHisOweFee(queryStatisticsDto);

    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorCurReceivableFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorCurReceivableFee(queryStatisticsDto);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorCurReceivedFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorCurReceivedFee(queryStatisticsDto);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorHisReceivedFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorHisReceivedFee(queryStatisticsDto);
    }
}
