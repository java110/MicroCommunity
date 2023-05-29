package com.java110.report.statistics;


import com.java110.dto.report.QueryStatisticsDto;

/**
 * 出入统计类
 */
public interface IInoutStatistics {


    /**
     * 查询进场车辆数
     * @param queryStatisticsDto
     * @return
     */
    long getCarInCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询出场车辆数
     * @param queryStatisticsDto
     * @return
     */
    long getCarOutCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询进场人员数
     * @param queryStatisticsDto
     * @return
     */
    long getPersonInCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询 人脸同步数
     * @param queryStatisticsDto
     * @return
     */
    long getPersonFaceToMachineCount(QueryStatisticsDto queryStatisticsDto);


    /**
     * 采购入库数
     * @param queryStatisticsDto
     * @return
     */
    long purchaseInCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 领用出库数
     * @param queryStatisticsDto
     * @return
     */
    long purchaseOutCount(QueryStatisticsDto queryStatisticsDto);


    /**
     * 采购入库金额
     * @param queryStatisticsDto
     * @return
     */
    double purchaseInAmount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 领用出库金额
     * @param queryStatisticsDto
     * @return
     */
    double purchaseOutAmount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 调拨数量
     * @param queryStatisticsDto
     * @return
     */
    long allocationCount(QueryStatisticsDto queryStatisticsDto);


    /**
     * 房屋装修数
     * @param queryStatisticsDto
     * @return
     */
    long roomRenovationCount(QueryStatisticsDto queryStatisticsDto);


    /**
     * 物品放行
     * @param queryStatisticsDto
     * @return
     */
    long itemReleaseCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 交房数量
     * @param queryStatisticsDto
     * @return
     */
    long roomInCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 退房数量
     * @param queryStatisticsDto
     * @return
     */
    long roomOutCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 业主绑定
     * @param queryStatisticsDto
     * @return
     */
    long ownerRegisterCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 未考勤数
     * @param queryStatisticsDto
     * @return
     */
    long noAttendanceCount(QueryStatisticsDto queryStatisticsDto);
}
