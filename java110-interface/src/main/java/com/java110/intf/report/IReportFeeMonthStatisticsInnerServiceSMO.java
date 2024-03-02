package com.java110.intf.report;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.room.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.report.ReportDeposit;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.java110.po.reportFee.ReportFeeMonthStatisticsPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IReportFeeMonthStatisticsInnerServiceSMO
 * @Description 费用月统计接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportFeeMonthStatisticsApi")
public interface IReportFeeMonthStatisticsInnerServiceSMO {


    @RequestMapping(value = "/saveReportFeeMonthStatistics", method = RequestMethod.POST)
    public int saveReportFeeMonthStatistics(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo);

    @RequestMapping(value = "/updateReportFeeMonthStatistics", method = RequestMethod.POST)
    public int updateReportFeeMonthStatistics(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo);

    @RequestMapping(value = "/deleteReportFeeMonthStatistics", method = RequestMethod.POST)
    public int deleteReportFeeMonthStatistics(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param reportFeeMonthStatisticsDto 数据对象分享
     * @return ReportFeeMonthStatisticsDto 对象数据
     */
    @RequestMapping(value = "/queryReportFeeMonthStatisticss", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryReportFeeMonthStatisticss(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportFeeMonthStatisticsDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportFeeMonthStatisticssCount", method = RequestMethod.POST)
    int queryReportFeeMonthStatisticssCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);


    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryReportFeeSummaryCount", method = RequestMethod.POST)
    int queryReportFeeSummaryCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryReportFeeSummary", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryReportFeeSummary(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryReportFeeSummaryDetailCount", method = RequestMethod.POST)
    int queryReportFeeSummaryDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryReportFeeSummaryDetail", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryReportFeeSummaryDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表 大计
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryReportFeeSummaryMajor", method = RequestMethod.POST)
    ReportFeeMonthStatisticsDto queryReportFeeSummaryMajor(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryReportFloorUnitFeeSummaryCount", method = RequestMethod.POST)
    int queryReportFloorUnitFeeSummaryCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryReportFloorUnitFeeSummary", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryReportFloorUnitFeeSummary(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryReportFloorUnitFeeSummaryDetailCount", method = RequestMethod.POST)
    int queryReportFloorUnitFeeSummaryDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryReportFloorUnitFeeDetailSummary", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryReportFloorUnitFeeDetailSummary(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);


    @RequestMapping(value = "/queryReportFloorUnitFeeSummaryMajor", method = RequestMethod.POST)
    ReportFeeMonthStatisticsDto queryReportFloorUnitFeeSummaryMajor(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryFeeBreakdownCount", method = RequestMethod.POST)
    int queryFeeBreakdownCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryFeeBreakdown", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryFeeBreakdown(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryFeeBreakdownDetailCount", method = RequestMethod.POST)
    int queryFeeBreakdownDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryFeeBreakdownDetail", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryFeeBreakdownDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);


    @RequestMapping(value = "/queryFeeBreakdownMajor", method = RequestMethod.POST)
    ReportFeeMonthStatisticsDto queryFeeBreakdownMajor(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryFeeDetailCount", method = RequestMethod.POST)
    int queryFeeDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表总费用
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryAllFeeDetail", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryAllFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryFeeDetail", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryPrePaymentNewCount", method = RequestMethod.POST)
    int queryPrePaymentNewCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryPrePayment", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryPrePayment(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryOweFeeDetailCount", method = RequestMethod.POST)
    int queryOweFeeDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryOweFeeDetail", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryOweFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    @RequestMapping(value = "/queryOweFeeDetailMajor", method = RequestMethod.POST)
    ReportFeeMonthStatisticsDto queryOweFeeDetailMajor(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryPayFeeDetailCount", method = RequestMethod.POST)
    JSONObject queryPayFeeDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryPayFeeDetail", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryPayFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);


    /**
     * 查询账户抵扣金额(大计)
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryFeeAccountDetailSum", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryFeeAccountDetailSum(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表总费用
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryAllPayFeeDetail", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryAllPayFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询(优惠、减免、滞纳金、空置房打折、空置房减免等)总金额
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryPayFeeDetailSum", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryPayFeeDetailSum(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryDeadlineFeeCount", method = RequestMethod.POST)
    int queryDeadlineFeeCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询优惠金额
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryPayFeeDetailDiscount", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryPayFeeDetailDiscount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryDeadlineFee", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryDeadlineFee(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询预付费户数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryPrePaymentCount", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryPrePaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询预付费户数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryDeadlinePaymentCount", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryDeadlinePaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询预付费户数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryOwePaymentCount", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryOwePaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询预付费户数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryAllPaymentCount", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryAllPaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    @RequestMapping(value = "/queryReportProficientCount", method = RequestMethod.POST)
    JSONObject queryReportProficientCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param feeConfigDto 数据对象分享
     * @return FeeConfigDto 对象数据
     */
    @RequestMapping(value = "/queryFeeConfigs", method = RequestMethod.POST)
    List<FeeConfigDto> queryFeeConfigs(@RequestBody FeeConfigDto feeConfigDto);

    /**
     * 查询维修师傅报修信息
     *
     * @param repairUserDto
     * @return
     */
    @RequestMapping(value = "/queryRepair", method = RequestMethod.POST)
    List<RepairUserDto> queryRepair(@RequestBody RepairUserDto repairUserDto);

    /**
     * 查询报修信息
     *
     * @param repairUserDto
     * @return
     */
    @RequestMapping(value = "/queryRepairWithOutPage", method = RequestMethod.POST)
    List<RepairUserDto> queryRepairWithOutPage(@RequestBody RepairUserDto repairUserDto);

    /**
     * 查询员工报修表员工信息
     *
     * @param repairUserDto
     * @return
     */
    @RequestMapping(value = "/queryRepairForStaff", method = RequestMethod.POST)
    List<RepairUserDto> queryRepairForStaff(@RequestBody RepairUserDto repairUserDto);

    /**
     * 查询未收费房屋
     *
     * @param roomDto
     * @return
     */
    @RequestMapping(value = "/queryNoFeeRoomsCount", method = RequestMethod.POST)
    int queryNoFeeRoomsCount(@RequestBody RoomDto roomDto);

    /**
     * 查询未收费房屋
     *
     * @param roomDto 房屋
     * @return
     */
    @RequestMapping(value = "/queryNoFeeRooms", method = RequestMethod.POST)
    List<RoomDto> queryNoFeeRooms(@RequestBody RoomDto roomDto);

    /**
     * 查询押金
     *
     * @param reportDeposit
     * @return
     */
    @RequestMapping(value = "/queryFeeDeposit", method = RequestMethod.POST)
    List<ReportDeposit> queryPayFeeDeposit(@RequestBody ReportDeposit reportDeposit);

    /**
     * 查询押金退费总金额
     */
    @RequestMapping(value = "/queryFeeDepositAmount", method = RequestMethod.POST)
    List<ReportDeposit> queryFeeDepositAmount(@RequestBody ReportDeposit reportDeposit);


    /**
     * 查询华宁物业 欠费总数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryHuaningOweFeeCount", method = RequestMethod.POST)
    int queryHuaningOweFeeCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询华宁物业 欠费明细 按楼栋 group by
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryHuaningOweFee", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryHuaningOweFee(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    @RequestMapping(value = "/queryHuaningOweFeeCounts", method = RequestMethod.POST)
    int queryHuaningOweFeeCounts(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    @RequestMapping(value = "/queryHuaningPayFeeCount", method = RequestMethod.POST)
    int queryHuaningPayFeeCount(@RequestBody Map paramInfo);

    @RequestMapping(value = "/queryHuaningPayFee", method = RequestMethod.POST)
    List<Map> queryHuaningPayFee(@RequestBody Map paramInfo);

    @RequestMapping(value = "/queryHuaningPayFeeCounts", method = RequestMethod.POST)
    int queryHuaningPayFeeCounts(@RequestBody Map paramInfo);

    @RequestMapping(value = "/queryHuaningPayFeeTwoCount", method = RequestMethod.POST)
    int queryHuaningPayFeeTwoCount(@RequestBody Map paramInfo);

    @RequestMapping(value = "/queryHuaningPayFeeTwo", method = RequestMethod.POST)
    List<Map> queryHuaningPayFeeTwo(@RequestBody Map paramInfo);

    @RequestMapping(value = "/queryHuaningOweFeeDetailCount", method = RequestMethod.POST)
    int queryHuaningOweFeeDetailCount(@RequestBody Map paramInfo);

    @RequestMapping(value = "/queryHuaningOweFeeDetail", method = RequestMethod.POST)
    List<Map> queryHuaningOweFeeDetail(@RequestBody Map paramInfo);

    /**
     * @param ownerDto
     * @return
     */
    @RequestMapping(value = "/queryRoomAndParkingSpace", method = RequestMethod.POST)
    List<OwnerDto> queryRoomAndParkingSpace(@RequestBody OwnerDto ownerDto);


}
