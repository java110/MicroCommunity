package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.dto.reportOweFee.ReportOweFeeItemDto;
import com.java110.po.reportOweFee.ReportOweFeePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IReportOweFeeInnerServiceSMO
 * @Description 欠费统计接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportOweFeeApi")
public interface IReportOweFeeInnerServiceSMO {


    @RequestMapping(value = "/saveReportOweFee", method = RequestMethod.POST)
    public int saveReportOweFee(@RequestBody ReportOweFeePo reportOweFeePo);

    @RequestMapping(value = "/updateReportOweFee", method = RequestMethod.POST)
    public int updateReportOweFee(@RequestBody ReportOweFeePo reportOweFeePo);

    @RequestMapping(value = "/deleteReportOweFee", method = RequestMethod.POST)
    public int deleteReportOweFee(@RequestBody ReportOweFeePo reportOweFeePo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param reportOweFeeDto 数据对象分享
     * @return ReportOweFeeDto 对象数据
     */
    @RequestMapping(value = "/queryReportOweFees", method = RequestMethod.POST)
    List<ReportOweFeeDto> queryReportOweFees(@RequestBody ReportOweFeeDto reportOweFeeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportOweFeeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportOweFeesCount", method = RequestMethod.POST)
    int queryReportOweFeesCount(@RequestBody ReportOweFeeDto reportOweFeeDto);

    /**
     * 查询所有 欠费信息
     *
     * @param reportOweFeeDto
     * @return
     */
    @RequestMapping(value = "/queryReportAllOweFees", method = RequestMethod.POST)
    List<ReportOweFeeDto> queryReportAllOweFees(@RequestBody ReportOweFeeDto reportOweFeeDto);

    /**
     * 查询欠费信息(与房屋关联)
     */
    @RequestMapping(value = "/queryReportAllOweFeesByRoom", method = RequestMethod.POST)
    List<ReportOweFeeDto> queryReportAllOweFeesByRoom(@RequestBody ReportOweFeeDto reportOweFeeDto);

    /**
     * 查询欠费信息(与车辆关联)
     */
    @RequestMapping(value = "/queryReportAllOweFeesByCar", method = RequestMethod.POST)
    List<ReportOweFeeDto> queryReportAllOweFeesByCar(@RequestBody ReportOweFeeDto reportOweFeeDto);

    /**
     * 查询欠费信息(与车辆关联)
     */
    @RequestMapping(value = "/queryReportAllOweFeesByContract", method = RequestMethod.POST)
    List<ReportOweFeeDto> queryReportAllOweFeesByContract(@RequestBody ReportOweFeeDto reportOweFeeDto);

    /**
     * 计算总欠费
     * @param reportOweFeeDto
     * @return
     */
    @RequestMapping(value = "/computeReportOweFeeTotalAmount", method = RequestMethod.POST)
    double computeReportOweFeeTotalAmount(@RequestBody ReportOweFeeDto reportOweFeeDto);
    @RequestMapping(value = "/computeReportOweFeeItemAmount", method = RequestMethod.POST)
    List<ReportOweFeeItemDto> computeReportOweFeeItemAmount(@RequestBody ReportOweFeeDto reportOweFeeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportOweFeeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deleteInvalidFee", method = RequestMethod.POST)
    int deleteInvalidFee(@RequestBody Map reportOweFeeDto);

    @RequestMapping(value = "/queryOweFeesByOwnerIds", method = RequestMethod.POST)
    List<Map> queryOweFeesByOwnerIds(@RequestBody Map info);

    @RequestMapping(value = "/queryOweFeesByRoomIds", method = RequestMethod.POST)
    List<Map> queryOweFeesByRoomIds(@RequestBody Map info);
}
