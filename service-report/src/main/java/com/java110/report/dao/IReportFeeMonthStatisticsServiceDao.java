package com.java110.report.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 费用月统计组件内部之间使用，没有给外围系统提供服务能力
 * 费用月统计服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IReportFeeMonthStatisticsServiceDao {


    /**
     * 保存 费用月统计信息
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveReportFeeMonthStatisticsInfo(Map info) throws DAOException;


    /**
     * 查询费用月统计信息（instance过程）
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> getReportFeeMonthStatisticsInfo(Map info) throws DAOException;


    /**
     * 修改费用月统计信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateReportFeeMonthStatisticsInfo(Map info) throws DAOException;


    /**
     * 修改费用月统计信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateReportFeeMonthStatisticsOwe(Map info) throws DAOException;


    /**
     * 查询费用月统计总数
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    int queryReportFeeMonthStatisticssCount(Map info);

    /**
     * 查询费用月统计总数
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    int queryReportFeeSummaryCount(Map info);


    /**
     * 查询费用月统计信息（instance过程）
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryReportFeeSummary(Map info) throws DAOException;

    /**
     * 查询费用月统计总数
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    int queryReportFeeSummaryDetailCount(Map info);


    /**
     * 查询费用月统计信息（instance过程）
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryReportFeeSummaryDetail(Map info) throws DAOException;


    /**
     * 查询费用月统计总数
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    int queryReportFloorUnitFeeSummaryCount(Map info);


    /**
     * 查询费用月统计信息（instance过程）
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryReportFloorUnitFeeSummary(Map info) throws DAOException;


    /**
     * 查询费用月统计总数
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    int queryReportFloorUnitFeeSummaryDetailCount(Map info);


    /**
     * 查询费用月统计信息（instance过程）
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryReportFloorUnitFeeSummaryDetail(Map info) throws DAOException;


    /**
     * 查询费用月统计总数
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    int queryFeeBreakdownCount(Map info);


    /**
     * 查询费用月统计信息（instance过程）
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryFeeBreakdown(Map info) throws DAOException;

    /**
     * 查询费用月统计总数
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    int queryFeeBreakdownDetailCount(Map info);


    /**
     * 查询费用月统计信息（instance过程）
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryFeeBreakdownDetail(Map info) throws DAOException;

    /**
     * 查询费用月统计总数
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    int queryFeeDetailCount(Map info);


    /**
     * 查询费用月统计信息（instance过程）
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryFeeDetail(Map info) throws DAOException;


    /**
     * 查询费用月统计总数
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    int queryOweFeeDetailCount(Map info);

    Map queryPayFeeDetailCount(Map info);

    int queryDeadlineFeeCount(Map info);

    /**
     * 查询费用月统计信息（instance过程）
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryPrePayment(Map info) throws DAOException;


    /**
     * 查询费用月统计总数
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    int queryPrePaymentNewCount(Map info);


    /**
     * 查询费用月统计信息（instance过程）
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryOweFeeDetail(Map info) throws DAOException;

    List<Map> queryPayFeeDetail(Map info) throws DAOException;

    List<Map> queryAllPayFeeDetail(Map info) throws DAOException;

    List<Map> queryDeadlineFee(Map info) throws DAOException;

    /**
     * 查询预交费
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryPrePaymentCount(Map info) throws DAOException;

    List<Map> queryDeadlinePaymentCount(Map info) throws DAOException;

    /**
     * 查询预交费
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryOwePaymentCount(Map info) throws DAOException;


    /**
     * 查询 缴费已经结束 报表表还有欠费的数据
     * 根据bId 查询费用月统计信息
     *
     * @param info bId 信息
     * @return 费用月统计信息
     * @throws DAOException DAO异常
     */
    List<Map> queryFinishOweFee(Map info) throws DAOException;


    List<Map> queryAllPaymentCount(Map beanCovertMap);

    /**
     * 查询费用汇总表总金额
     *
     * @param beanCovertMap
     * @return
     */
    List<Map> queryAllFeeDetail(Map beanCovertMap);

    /**
     * 查询(优惠、减免、滞纳金、空置房打折、空置房减免等)总金额
     *
     * @param beanCovertMap
     * @return
     */
    List<Map> queryPayFeeDetailSum(Map beanCovertMap);

    /**
     * 查询费用配置信息（instance过程）
     * 根据bId 查询费用配置信息
     *
     * @param info bId 信息
     * @return 费用配置信息
     * @throws DAOException DAO异常
     */
    List<Map> getFeeConfigInfo(Map info) throws DAOException;

    /**
     * 查询维修师傅报修信息
     *
     * @param info
     * @return
     */
    List<Map> getRepairUserInfo(Map info);

    /**
     * 查询报修信息
     *
     * @param info
     * @return
     */
    List<Map> getRepairWithOutPage(Map info);

    /**
     * 获取报修员工表员工信息
     *
     * @param info
     * @return
     */
    List<Map> getRepairStaff(Map info);

    /**
     * 计算应收 信息
     * @param beanCovertMap
     * @return
     */
    Map getReceivableInformation(Map beanCovertMap);

    /**
     * 计算应收 信息
     * @param beanCovertMap
     * @return
     */
    List<Map> getFloorReceivableInformation(Map beanCovertMap);

    /**
     * 计算应收 信息
     * @param beanCovertMap
     * @return
     */
    List<Map> getFeeConfigReceivableInformation(Map beanCovertMap);

    int queryNoFeeRoomsCount(Map beanCovertMap);

    List<Map> queryNoFeeRooms(Map beanCovertMap);

    List<Map> queryPayFeeDeposit(Map beanCovertMap);

    List<Map> queryFeeDepositAmount(Map beanCovertMap);

    Map queryReportFeeSummaryMajor(Map beanCovertMap);

    Map queryReportFloorUnitFeeSummaryMajor(Map beanCovertMap);

    Map queryFeeBreakdownMajor(Map beanCovertMap);

    Map queryOweFeeDetailMajor(Map beanCovertMap);

    int queryHuaningOweFeeCount(Map beanCovertMap);

    List<Map> queryHuaningOweFee(Map beanCovertMap);

    int queryHuaningPayFeeCount(Map paramInfo);

    List<Map> queryHuaningPayFee(Map beanCovertMap);
    int queryHuaningPayFeeTwoCount(Map paramInfo);

    List<Map> queryHuaningPayFeeTwo(Map beanCovertMap);

    int queryHuaningOweFeeDetailCount(Map paramInfo);

    List<Map> queryHuaningOweFeeDetail(Map beanCovertMap);

    void deleteReportFeeMonthStatisticsInfo(Map beanCovertMap);

    /**
     * 查询当月实收
     * @param beanCovertMap
     * @return
     */
    double getReceivedAmountByMonth(Map beanCovertMap);

    /**
     *
     * @param info
     * @return
     */
    List<Map> queryRoomAndParkingSpace(Map info);

    int deleteInvalidFee(Map info);

    /**
     * 查询无效的数据
     * @param reportFeeDto
     * @return
     */
    List<Map> queryInvalidFeeMonthStatistics(Map reportFeeDto);
}
