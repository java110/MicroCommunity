package com.java110.report.bmo.reportFeeMonthStatistics;

import com.java110.dto.room.RoomDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.report.ReportDeposit;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IGetReportFeeMonthStatisticsBMO {


    /**
     * 查询费用月统计
     * add by wuxw
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    ResponseEntity<String> get(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用月统计
     * add by wuxw
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    ResponseEntity<String> queryReportFeeSummary(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    ResponseEntity<String> queryReportFloorUnitFeeSummary(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);


    ResponseEntity<String> queryFeeBreakdown(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    ResponseEntity<String> queryFeeDetail(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    ResponseEntity<String> queryOweFeeDetail(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);



    ResponseEntity<String> queryDeadlineFee(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    ResponseEntity<String> queryPrePaymentCount(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    ResponseEntity<String> queryDeadlinePaymentCount(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    ResponseEntity<String> queryPrePayment(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    ResponseEntity<String> queryOwePaymentCount(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询报表专家
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    ResponseEntity<String> queryReportProficientCount(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询报修信息
     *
     * @param repairUserDto
     * @return
     */
    ResponseEntity<String> queryRepair(RepairUserDto repairUserDto);

    /**
     * 查询未收费房屋
     * @param roomDto
     * @return
     */
    ResponseEntity<String> queryNoFeeRooms(RoomDto roomDto);

    /**
     * 查询押金
     * @param reportDeposit
     * @return
     */
    ResponseEntity<String> queryPayFeeDeposit(ReportDeposit reportDeposit);

    /**
     * 查询华宁物业欠费 统计
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    ResponseEntity<String> queryHuaningOweFee(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询华宁物业缴费 统计
     * @param paramInfo
     * @return
     */
    ResponseEntity<String> queryHuaningPayFee(Map paramInfo);

    ResponseEntity<String> queryHuaningPayFeeTwo(Map paramInfo);

    ResponseEntity<String> queryHuaningOweFeeDetail(Map paramInfo);
}
