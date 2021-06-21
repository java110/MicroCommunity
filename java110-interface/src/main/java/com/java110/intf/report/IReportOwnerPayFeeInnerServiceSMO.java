package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportOwnerPayFee.ReportOwnerPayFeeDto;
import com.java110.po.reportOwnerPayFee.ReportOwnerPayFeePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportOwnerPayFeeInnerServiceSMO
 * @Description 业主缴费明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportOwnerPayFeeApi")
public interface IReportOwnerPayFeeInnerServiceSMO {


    @RequestMapping(value = "/saveReportOwnerPayFee", method = RequestMethod.POST)
    public int saveReportOwnerPayFee(@RequestBody ReportOwnerPayFeePo reportOwnerPayFeePo);

    @RequestMapping(value = "/updateReportOwnerPayFee", method = RequestMethod.POST)
    public int updateReportOwnerPayFee(@RequestBody  ReportOwnerPayFeePo reportOwnerPayFeePo);

    @RequestMapping(value = "/deleteReportOwnerPayFee", method = RequestMethod.POST)
    public int deleteReportOwnerPayFee(@RequestBody  ReportOwnerPayFeePo reportOwnerPayFeePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportOwnerPayFeeDto 数据对象分享
     * @return ReportOwnerPayFeeDto 对象数据
     */
    @RequestMapping(value = "/queryReportOwnerPayFees", method = RequestMethod.POST)
    List<ReportOwnerPayFeeDto> queryReportOwnerPayFees(@RequestBody ReportOwnerPayFeeDto reportOwnerPayFeeDto);

    /**
     * <p>查询月费用</p>
     *
     *
     * @param reportOwnerPayFeeDto 数据对象分享
     * @return ReportOwnerPayFeeDto 对象数据
     */
    @RequestMapping(value = "/queryReportOwnerMonthPayFees", method = RequestMethod.POST)
    List<ReportOwnerPayFeeDto> queryReportOwnerMonthPayFees(@RequestBody ReportOwnerPayFeeDto reportOwnerPayFeeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportOwnerPayFeeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportOwnerPayFeesCount", method = RequestMethod.POST)
    int queryReportOwnerPayFeesCount(@RequestBody ReportOwnerPayFeeDto reportOwnerPayFeeDto);
}
