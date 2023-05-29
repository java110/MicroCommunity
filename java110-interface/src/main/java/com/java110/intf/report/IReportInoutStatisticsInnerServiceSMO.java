package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.report.QueryStatisticsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName IReportFeeStatisticsInnerServiceSMO
 * @Description 费用统计类 服务类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportInoutStatisticsApi")
public interface IReportInoutStatisticsInnerServiceSMO {


    @RequestMapping(value = "/getCarInCount", method = RequestMethod.POST)
    long getCarInCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getCarOutCount", method = RequestMethod.POST)
    long getCarOutCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getPersonInCount", method = RequestMethod.POST)
    long getPersonInCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getPersonFaceToMachineCount", method = RequestMethod.POST)
    long getPersonFaceToMachineCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/purchaseInCount", method = RequestMethod.POST)
    long purchaseInCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/purchaseOutCount", method = RequestMethod.POST)
    long purchaseOutCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/purchaseInAmount", method = RequestMethod.POST)
    double purchaseInAmount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/purchaseOutAmount", method = RequestMethod.POST)
    double purchaseOutAmount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/allocationCount", method = RequestMethod.POST)
    long allocationCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/roomRenovationCount", method = RequestMethod.POST)
    long roomRenovationCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/itemReleaseCount", method = RequestMethod.POST)
    long itemReleaseCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/roomInCount", method = RequestMethod.POST)
    long roomInCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/roomOutCount", method = RequestMethod.POST)
    long roomOutCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/ownerRegisterCount", method = RequestMethod.POST)
    long ownerRegisterCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/noAttendanceCount", method = RequestMethod.POST)
    long noAttendanceCount(@RequestBody QueryStatisticsDto queryStatisticsDto);
}
