package com.java110.report.dao;

import java.util.List;
import java.util.Map;

/**
 * 费用统计 dao 层
 */
public interface IReportFloorFeeStatisticsServiceDao {

    List<Map> getFloorOweRoomCount(Map info);

    List<Map> getFloorFeeRoomCount(Map info);

    List<Map> getFloorReceivedFee(Map info);

    List<Map> getFloorPreReceivedFee(Map info);

    List<Map> getFloorHisOweFee(Map info);

    List<Map> getFloorCurReceivableFee(Map info);

    List<Map> getFloorCurReceivedFee(Map info);

    List<Map> getFloorHisReceivedFee(Map info);
}
