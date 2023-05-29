package com.java110.report.statistics;


import com.java110.dto.report.QueryStatisticsDto;

/**
 * 其他统计类
 */
public interface IOthersStatistics {


    /**
     * 查询场地预约数
     * @param queryStatisticsDto
     * @return
     */
    long venueReservationCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询 合同数
     * @param queryStatisticsDto
     * @return
     */
    long contractCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询资产变更数
     * @param queryStatisticsDto
     * @return
     */
    long contractChangeCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询 租期变更
     * @param queryStatisticsDto
     * @return
     */
    long leaseChangeCount(QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询主体变更
     * @param queryStatisticsDto
     * @return
     */
    long mainChange(QueryStatisticsDto queryStatisticsDto);

    /**
     * 到期合同
     * @param queryStatisticsDto
     * @return
     */
    long expirationContract(QueryStatisticsDto queryStatisticsDto);


    /**
     * 车辆数
     * @param queryStatisticsDto
     * @return
     */
    long carCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 车位申请数
     * @param queryStatisticsDto
     * @return
     */
    long carApplyCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 停车券购买数
     * @param queryStatisticsDto
     * @return
     */
    double buyParkingCouponCount(QueryStatisticsDto queryStatisticsDto);


    /**
     * 停车券核销
     * @param queryStatisticsDto
     * @return
     */
    long writeOffParkingCouponCount(QueryStatisticsDto queryStatisticsDto);


    /**
     * 赠送优惠券
     * @param queryStatisticsDto
     * @return
     */
    double sendCouponCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 核销优惠券
     * @param queryStatisticsDto
     * @return
     */
    long writeOffCouponCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 赠送积分
     * @param queryStatisticsDto
     * @return
     */
    double sendIntegralCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 核销积分
     * @param queryStatisticsDto
     * @return
     */
    double writeOffIntegralCount(QueryStatisticsDto queryStatisticsDto);


}
