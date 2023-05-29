package com.java110.report.dao;

import java.util.Map;

/**
 * 费用统计 dao 层
 */
public interface IReportInoutStatisticsServiceDao {

    long getCarInCount(Map info);

    long getCarOutCount(Map info);

    long getPersonInCount(Map info);

    long getPersonFaceToMachineCount(Map info);

    long purchaseInCount(Map info);

    long purchaseOutCount(Map info);

    double purchaseInAmount(Map info);

    double purchaseOutAmount(Map info);

    long allocationCount(Map info);

    long roomRenovationCount(Map info);

    long itemReleaseCount(Map info);

    long roomInCount(Map info);

    long roomOutCount(Map info);

    long ownerRegisterCount(Map info);

    long noAttendanceCount(Map info);
}
