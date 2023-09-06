package com.java110.intf.report;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.po.ReportFeeMonthStatisticsPrepaymentPo.ReportFeeMonthStatisticsPrepaymentPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author fqz
 * @descrip0tion预付期费用月统计接口类
 * @date 2023-03-29 09:46
 */
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportFeeMonthStatisticsPrepaymentApi")
public interface IReportFeeMonthStatisticsPrepaymentInnerServiceSMO {

    @RequestMapping(value = "/saveReportFeeMonthStatisticsPrepayment", method = RequestMethod.POST)
    public int saveReportFeeMonthStatisticsPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo);

    @RequestMapping(value = "/updateReportFeeMonthStatisticsPrepayment", method = RequestMethod.POST)
    public int updateReportFeeMonthStatisticsPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo);

    @RequestMapping(value = "/deleteReportFeeMonthStatisticsPrepayment", method = RequestMethod.POST)
    public int deleteReportFeeMonthStatisticsPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param reportFeeMonthStatisticsPrepaymentDto 数据对象分享
     * @return ReportFeeMonthStatisticsPrepaymentDto 对象数据
     */
    @RequestMapping(value = "/queryReportFeeMonthStatisticsPrepayment", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsPrepaymentDto> queryReportFeeMonthStatisticsPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param reportFeeMonthStatisticsPrepaymentDto 数据对象分享
     * @return ReportFeeMonthStatisticsPrepaymentDto 对象数据
     */
    @RequestMapping(value = "/queryPrepaymentConfigs", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsPrepaymentDto> queryPrepaymentConfigs(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    @RequestMapping(value = "/queryAllPrepaymentConfigs", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsPrepaymentDto> queryAllPrepaymentConfigs(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    @RequestMapping(value = "/queryAllPrepaymentDiscounts", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsPrepaymentDto> queryAllPrepaymentDiscounts(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportFeeMonthStatisticsPrepaymentDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportFeeMonthStatisticsPrepaymentsCount", method = RequestMethod.POST)
    int queryReportFeeMonthStatisticsPrepaymentsCount(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsPrepaymentDto
     * @return
     */
    @RequestMapping(value = "/queryPayFeeDetailPrepaymentCount", method = RequestMethod.POST)
    JSONObject queryPayFeeDetailPrepaymentCount(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    /**
     * 查询费用汇总表个数
     *
     * @param reportFeeMonthStatisticsPrepaymentDto
     * @return
     */
    @RequestMapping(value = "/queryReportCollectFeesCount", method = RequestMethod.POST)
    JSONObject queryReportCollectFeesCount(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    /**
     * 查询费用汇总表
     *
     * @param reportFeeMonthStatisticsPrepaymentDto
     * @return
     */
    @RequestMapping(value = "/queryPayFeeDetailPrepayment", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsPrepaymentDto> queryPayFeeDetailPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    @RequestMapping(value = "/queryNewPayFeeDetailPrepayment",method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsPrepaymentDto> queryNewPayFeeDetailPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    /**
     * 查询费用汇总表总费用
     *
     * @param reportFeeMonthStatisticsPrepaymentDto
     * @return
     */
    @RequestMapping(value = "/queryAllPayFeeDetailPrepayment", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsPrepaymentDto> queryAllPayFeeDetailPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    /**
     * 查询(优惠、减免、滞纳金、空置房打折、空置房减免等)总金额
     *
     * @param reportFeeMonthStatisticsPrepaymentDto
     * @return
     */
    @RequestMapping(value = "/queryPayFeeDetailPrepaymentSum", method = RequestMethod.POST)
    List<ReportFeeMonthStatisticsPrepaymentDto> queryPayFeeDetailPrepaymentSum(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param feeConfigDto 数据对象分享
     * @return FeeConfigDto 对象数据
     */
    @RequestMapping(value = "/queryFeeConfigPrepayments", method = RequestMethod.POST)
    List<FeeConfigDto> queryFeeConfigPrepayments(@RequestBody FeeConfigDto feeConfigDto);

    /**
     * @param ownerDto
     * @return
     */
    @RequestMapping(value = "/queryRoomAndParkingSpacePrepayment", method = RequestMethod.POST)
    List<OwnerDto> queryRoomAndParkingSpacePrepayment(@RequestBody OwnerDto ownerDto);

}
