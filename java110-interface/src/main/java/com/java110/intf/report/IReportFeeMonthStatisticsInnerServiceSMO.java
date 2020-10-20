package com.java110.intf;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

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
    @RequestMapping(value = "/queryFeeDetailCount", method = RequestMethod.POST)
    int queryFeeDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

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
    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @RequestMapping(value = "/queryDeadlineFeeCount", method = RequestMethod.POST)
    int queryDeadlineFeeCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

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
    @RequestMapping(value = "/queryOwePaymentCount", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsDto> queryOwePaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);


}
