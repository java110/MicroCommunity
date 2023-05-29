package com.java110.report.dao;

import java.util.Map;

/**
 * 费用统计 dao 层
 */
public interface IReportOthersStatisticsServiceDao {

    long venueReservationCount(Map info);

    long contractCount(Map info);

    long contractChangeCount(Map info);

    long leaseChangeCount(Map info);

    long mainChange(Map info);

    long expirationContract(Map info);

    long carCount(Map info);

    long carApplyCount(Map info);

    double buyParkingCouponCount(Map info);

    long writeOffParkingCouponCount(Map info);

    double sendCouponCount(Map info);

    long writeOffCouponCount(Map info);

    double sendIntegralCount(Map info);

    double writeOffIntegralCount(Map info);
}
