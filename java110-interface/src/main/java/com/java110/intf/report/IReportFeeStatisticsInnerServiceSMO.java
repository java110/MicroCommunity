package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.report.QueryStatisticsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportFeeStatisticsInnerServiceSMO
 * @Description 费用统计类 服务类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportFeeStatisticsApi")
public interface IReportFeeStatisticsInnerServiceSMO {


    /**
     * <p>查询历史月欠费</p>
     *
     * @param queryFeeStatisticsDto 数据对象分享
     */
    @RequestMapping(value = "/getHisMonthOweFee", method = RequestMethod.POST)
    double getHisMonthOweFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto);

    /**
     * 查询当月欠费
     * @param queryFeeStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getCurMonthOweFee", method = RequestMethod.POST)
    double getCurMonthOweFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto);

    @RequestMapping(value = "/getHisReceivedFee", method = RequestMethod.POST)
    double getHisReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto);

    @RequestMapping(value = "/getPreReceivedFee", method = RequestMethod.POST)
    double getPreReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto);

    @RequestMapping(value = "/getReceivedFee", method = RequestMethod.POST)
    double getReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto);

    /**
     * 欠费户数
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getOweRoomCount", method = RequestMethod.POST)
    int getOweRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto);
}
