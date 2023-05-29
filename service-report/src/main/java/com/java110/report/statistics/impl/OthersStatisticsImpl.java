package com.java110.report.statistics.impl;

import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportInoutStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportOthersStatisticsInnerServiceSMO;
import com.java110.report.statistics.IInoutStatistics;
import com.java110.report.statistics.IOthersStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 其他报表统计 实现类
 */
@Service
public class OthersStatisticsImpl implements IOthersStatistics {

    @Autowired
    private IReportOthersStatisticsInnerServiceSMO reportOthersStatisticsInnerServiceSMOImpl;



    @Override
    public long venueReservationCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.venueReservationCount(queryStatisticsDto);
    }

    @Override
    public long contractCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.contractCount(queryStatisticsDto);
    }

    @Override
    public long contractChangeCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.contractChangeCount(queryStatisticsDto);
    }

    @Override
    public long leaseChangeCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.leaseChangeCount(queryStatisticsDto);
    }

    @Override
    public long mainChange(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.mainChange(queryStatisticsDto);
    }

    @Override
    public long expirationContract(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.expirationContract(queryStatisticsDto);
    }

    @Override
    public long carCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.carCount(queryStatisticsDto);
    }

    @Override
    public long carApplyCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.carApplyCount(queryStatisticsDto);
    }

    @Override
    public double buyParkingCouponCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.buyParkingCouponCount(queryStatisticsDto);
    }

    @Override
    public long writeOffParkingCouponCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.writeOffParkingCouponCount(queryStatisticsDto);
    }

    @Override
    public double sendCouponCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.sendCouponCount(queryStatisticsDto);
    }

    @Override
    public long writeOffCouponCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.writeOffCouponCount(queryStatisticsDto);
    }

    @Override
    public double sendIntegralCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.sendIntegralCount(queryStatisticsDto);
    }

    @Override
    public double writeOffIntegralCount(QueryStatisticsDto queryStatisticsDto) {
        return reportOthersStatisticsInnerServiceSMOImpl.writeOffIntegralCount(queryStatisticsDto);
    }
}
