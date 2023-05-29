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
@RequestMapping("/reportOthersStatisticsApi")
public interface IReportOthersStatisticsInnerServiceSMO {


    @RequestMapping(value = "/getCarInCount", method = RequestMethod.POST)
    long venueReservationCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getCarOutCount", method = RequestMethod.POST)
    long contractCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getPersonInCount", method = RequestMethod.POST)
    long contractChangeCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getPersonFaceToMachineCount", method = RequestMethod.POST)
    long leaseChangeCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/purchaseInCount", method = RequestMethod.POST)
    long mainChange(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/purchaseOutCount", method = RequestMethod.POST)
    long expirationContract(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/purchaseInAmount", method = RequestMethod.POST)
    long carCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/purchaseOutAmount", method = RequestMethod.POST)
    long carApplyCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/allocationCount", method = RequestMethod.POST)
    double buyParkingCouponCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/roomRenovationCount", method = RequestMethod.POST)
    long writeOffParkingCouponCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/itemReleaseCount", method = RequestMethod.POST)
    double sendCouponCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/roomInCount", method = RequestMethod.POST)
    long writeOffCouponCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/roomOutCount", method = RequestMethod.POST)
    double sendIntegralCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/ownerRegisterCount", method = RequestMethod.POST)
    double writeOffIntegralCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

}
