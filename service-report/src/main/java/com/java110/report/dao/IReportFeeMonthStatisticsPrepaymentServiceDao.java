package com.java110.report.dao;

import java.util.List;
import java.util.Map;

public interface IReportFeeMonthStatisticsPrepaymentServiceDao {

    void saveReportFeeMonthStatisticsPrepaymentInfo(Map info);

    void updateReportFeeMonthStatisticsPrepaymentInfo(Map info);

    int queryReportFeeMonthStatisticsPrepaymentCount(Map info);

    void deleteReportFeeMonthStatisticsPrepaymentInfo(Map info);

    /**
     * 查询预付期费用月统计信息（instance过程）
     * 根据bId 查询预付期费用月统计信息
     *
     * @param info bId 信息
     * @return 预付期费用月统计信息
     */
    List<Map> getReportFeeMonthStatisticsPrepaymentInfo(Map info);

    List<Map> queryPrepaymentConfigs(Map info);

    List<Map> queryAllPrepaymentConfigs(Map info);

    List<Map> queryAllPrepaymentDiscounts(Map info);

    /**
     * 修改预付费费用月统计信息
     *
     * @param info 修改信息
     */
    void updateReportFeeMonthStatisticsPrepaymentOwe(Map info);

    /**
     * 查询无效的数据
     *
     * @param info
     * @return
     */
    List<Map> queryInvalidFeeMonthStatisticsPrepayment(Map info);

    int deleteInvalidFee(Map info);

    /**
     * 查询当月实收
     *
     * @param beanCovertMap
     * @return
     */
    double getReceivedAmountByMonth(Map beanCovertMap);

    /**
     * 查询 缴费已经结束 报表表还有欠费的数据
     * 根据bId 查询预付期费用月统计信息
     *
     * @param info bId 信息
     * @return 预付期费用月统计信息
     */
    List<Map> queryFinishOweFee(Map info);

    List<Map> queryPrePayment(Map info);

    Map queryPayFeeDetailCount(Map info);

    Map queryReportCollectFeesCount(Map info);

    List<Map> queryPayFeeDetail(Map info);

    List<Map> queryNewPayFeeDetail(Map info);

    List<Map> queryAllPayFeeDetail(Map info);

    List<Map> queryPayFeeDetailSum(Map beanCovertMap);

    List<Map> getFeeConfigInfo(Map info);

    List<Map> queryRoomAndParkingSpace(Map info);

}
